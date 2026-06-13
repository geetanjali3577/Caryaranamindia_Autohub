package com.autohub.serviceImpl;

import com.autohub.dto.*;
import com.autohub.emailservice.EmailService;
import com.autohub.entity.Dealer;
import com.autohub.enums.DealerStatus;
import com.autohub.enums.Role;
import com.autohub.enums.VehicleStatus;
import com.autohub.exception.ResourceNotFoundException;
import com.autohub.repository.DealerLeadRepository;
import com.autohub.repository.DealerRepository;
import com.autohub.repository.UserRepository;
import com.autohub.repository.VehicleRepository;
import com.autohub.service.DealerService;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DealerServiceImpl implements DealerService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final DealerRepository dealerRepository;
    private final VehicleRepository vehicleRepository;
    private final DealerLeadRepository dealerLeadRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ModelMapper modelMapper;

@Override
public DealerResponseDTO registerDealer(DealerRegisterDTO dto, MultipartFile dealerLogo, MultipartFile showroomImage) {

    if (dealerRepository.existsByEmail(dto.getEmail())) {
        throw new RuntimeException("Email already registered");
    }

    if (dealerRepository.existsByMobile(dto.getMobile())) {
        throw new RuntimeException("Mobile already registered");
    }

    validateImage(dealerLogo, "Dealer Logo");
    validateImage(showroomImage, "Showroom Image");

    Dealer dealer = new Dealer();
    dealer.setBusinessName(dto.getBusinessName());
    dealer.setOwnerName(dto.getOwnerName());
    dealer.setGstNumber(dto.getGstNumber());
    dealer.setYearsInBusiness(dto.getYearsInBusiness());
    dealer.setMobile(dto.getMobile());
    dealer.setWhatsapp(dto.getWhatsapp());
    dealer.setEmail(dto.getEmail());
    dealer.setPassword(passwordEncoder.encode(dto.getPassword()));
    dealer.setAddress(dto.getAddress());
    dealer.setDealerAccountStatus(DealerStatus.PENDING);
    dealer.setCity(dto.getCity());
    dealer.setState(dto.getState());
    dealer.setPinCode(dto.getPinCode());


    Dealer savedDealer = dealerRepository.save(dealer);

    System.out.println(savedDealer);

    String logoPath = saveFile(
            dealerLogo,
            String.valueOf(savedDealer.getId()),
            "logo"
    );

    String showroomPath = saveFile(
            showroomImage,
            String.valueOf(savedDealer.getId()),
            "showroom"
    );

    savedDealer.setDealerLogo(logoPath);
    savedDealer.setShowroomImage(showroomPath);

    savedDealer = dealerRepository.save(savedDealer);

    return modelMapper.map(savedDealer, DealerResponseDTO.class);

}

    private void validateImage(MultipartFile file, String fieldName) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException(fieldName + " is required");
        }

        String fileName = file.getOriginalFilename();

        if (fileName == null) {
            throw new RuntimeException(fieldName + " is invalid");
        }

        String extension =
                fileName.substring(fileName.lastIndexOf(".") + 1)
                        .toLowerCase();

        if (!extension.equals("jpg")
                && !extension.equals("jpeg")
                && !extension.equals("png")) {

            throw new RuntimeException(
                    fieldName + " must be JPG, JPEG or PNG format");
        }
    }


    private String saveFile(MultipartFile file,String dealerId,String prefix) {

        try {

            File directory = new File(uploadDir);

            if (!directory.exists()) {
                directory.mkdirs();
            }

            String extension =
                    file.getOriginalFilename()
                            .substring(
                                    file.getOriginalFilename()
                                            .lastIndexOf("."));

            String fileName =
                    dealerId +
                            "_" +
                            prefix +
                            extension;

            Path path =
                    Paths.get(uploadDir, fileName);

            Files.copy(
                    file.getInputStream(),
                    path,
                    StandardCopyOption.REPLACE_EXISTING);

            return path.toString();

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file");
        }
    }

    @Override
    public DealerProfileResponseDTO updateDealerProfile(Long id, UpdateDealerProfileRequestDTO dto) {

        Dealer dealer = dealerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Dealer Not Found"));
        dealer.setBusinessName(dto.getBusinessName());
        dealer.setOwnerName(dto.getOwnerName());
        dealer.setWhatsapp(dto.getWhatsapp());
        dealer.setAddress(dto.getAddress());
        dealer.setCity(dto.getCity());
        dealer.setState(dto.getState());

        Dealer save = dealerRepository.save(dealer);

        return modelMapper.map(save,DealerProfileResponseDTO.class);

    }


    @Override
    public DealerResponseDTO updateDealerAccountStatus(
            Long dealerId,
            DealerAccountStatusRequestDTO requestDTO) {

        Dealer dealer = dealerRepository.findById(dealerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Dealer not found"));

        if (requestDTO.getStatus() == null ||
                requestDTO.getStatus().trim().isEmpty()) {

            throw new RuntimeException("Status is required");
        }

        DealerStatus newStatus;

        try {
            newStatus = DealerStatus.valueOf(
                    requestDTO.getStatus().trim().toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException(
                    "Invalid status. Only PENDING and APPROVED are allowed");
        }

        DealerStatus currentStatus = dealer.getDealerAccountStatus();

        if (currentStatus == null) {
            currentStatus = DealerStatus.PENDING;
        }

        if (currentStatus.equals(newStatus)) {
            throw new RuntimeException(
                    "Dealer account already " + currentStatus);
        }

        dealer.setDealerAccountStatus(newStatus);

        Dealer updatedDealer = dealerRepository.save(dealer);

        return modelMapper.map(updatedDealer, DealerResponseDTO.class);
    }

    @Override
    public String sendOtp(String email) {

        Optional<Dealer> optionalDealer =  dealerRepository.findByEmail(email);

        if (optionalDealer.isEmpty()) {
            return "Dealer not found";
        }

        Dealer dealer = optionalDealer.get();

        // =====================================
        // CHECK 2 MINUTE LIMIT
        // =====================================

        if (dealer.getOtpGeneratedTime() != null) {

            LocalDateTime otpExpireTime = dealer.getOtpGeneratedTime()
                            .plusMinutes(2);

            if (LocalDateTime.now()
                    .isBefore(otpExpireTime)) {

                long secondsLeft =
                        Duration.between(
                                LocalDateTime.now(),
                                otpExpireTime
                        ).getSeconds();

                return "Please wait "
                        + secondsLeft +
                        " seconds before requesting new OTP";
            }
        }

        // =====================================
        // GENERATE OTP
        // =====================================

        int otp =
                (int) (Math.random() * 900000) + 100000;

        dealer.setOtp(String.valueOf(otp));

        dealer.setOtpGeneratedTime(
                LocalDateTime.now()
        );

        dealerRepository.save(dealer);

        // =====================================
        // SEND MAIL
        // =====================================

        String subject = "Forgot Password OTP";

        String body =
                "Dear " + dealer.getOwnerName() + ",\n\n" +

                        "We received a request to reset your password.\n\n" +

                        "Your OTP is : " + otp + "\n\n" +

                        "This OTP is valid for 5 minutes.\n\n" +

                        "Regards,\n" +
                        "Caryanam AutoHub Team";

        emailService.sendMail(
                dealer.getEmail(),
                subject,
                body
        );

        return "OTP sent successfully";
    }

    @Override
    public String verifyOtp(VerifyOtpDTO dto) {

        Optional<Dealer> optionalDealer =
                dealerRepository.findByEmail(dto.getEmail());

        if (optionalDealer.isEmpty()) {
            return "Dealer not found";
        }

        Dealer dealer = optionalDealer.get();

        if (dealer.getOtp() == null) {
            return "OTP not found";
        }

        if (!dealer.getOtp().equals(dto.getOtp())) {
            return "Invalid OTP";
        }

        if (dealer.getOtpGeneratedTime()
                .plusMinutes(5)
                .isBefore(LocalDateTime.now())) {

            return "OTP expired";
        }

        dealer.setIsOtpVerified(true);

        dealerRepository.save(dealer);

        return "OTP verified successfully";
    }

    @Override
    public String resetPassword(ResetPasswordDTO dto) {

        Optional<Dealer> optionalDealer =
                dealerRepository.findByEmail(dto.getEmail());

        if (optionalDealer.isEmpty()) {
            return "Dealer not found";
        }

        Dealer dealer = optionalDealer.get();

        if (dealer.getIsOtpVerified() == null
                || !dealer.getIsOtpVerified()) {

            return "Please verify OTP first";
        }
        // New Password Required
        if (dto.getNewPassword() == null
                || dto.getNewPassword().trim().isEmpty()) {

            return "New Password is Required";
        }

        // Password Length Validation
        if (dto.getNewPassword().length() < 8) {
            return "Password must be at least 8 characters";
        }

        // Same Password Validation
        if (passwordEncoder.matches(
                dto.getNewPassword(),
                dealer.getPassword())) {

            return "New Password cannot be same as Old Password";
        }



        // update password
        dealer.setPassword(
                passwordEncoder.encode(
                        dto.getNewPassword()
                )
        );

        // clear otp data
        dealer.setOtp(null);
        dealer.setOtpGeneratedTime(null);
        dealer.setIsOtpVerified(false);

        dealerRepository.save(dealer);

        // =====================================
        // SEND MAIL
        // =====================================

        String subject =
                "Password Reset Successfully";

        String body =
                "Dear " + dealer.getOwnerName() + ",\n\n" +

                        "Your password has been reset successfully.\n\n" +

                        "If you did not perform this action, " +
                        "please contact support immediately.\n\n" +

                        "Regards,\n" +
                        "Caryanam AutoHub Team";

        emailService.sendMail(
                dealer.getEmail(),
                subject,
                body
        );

        return "Password reset successfully";
    }


    @Override
    public List<DealerSubscriptionResponseDTO> getSubscriptions() {

        return dealerRepository.findAll()
                .stream()
                .map(dealer -> {

                    DealerSubscriptionResponseDTO dto =
                            new DealerSubscriptionResponseDTO();

                    dto.setId((dealer.getId()));

                    dto.setDealerName(dealer.getBusinessName());


                    dto.setSubscriptionActive(
                            dealer.getSubscriptionActive());

                    dto.setSubscriptionPlan(
                            dealer.getSubscriptionPlan());

                    return dto;

                }).toList();
    }




    @Override
    public DashboardResponseDTO getDashboard(Long dealerId) {

        Dealer dealer = dealerRepository.findById(dealerId)
                .orElseThrow(() ->
                        new RuntimeException("Dealer not found"));

        DashboardResponseDTO dto = new DashboardResponseDTO();

        dto.setDealerName(dealer.getBusinessName());

        dto.setTotalVehicles(
                vehicleRepository.countByDealer_Id(
                        dealer.getId()));

        dto.setFeaturedVehicles(
                vehicleRepository.countByDealer_IdAndVehicleStatus(
                        dealer.getId(),
                        VehicleStatus.FEATURED));

        dto.setTotalLeads(
                dealerLeadRepository.countByDealer_Id(
                        dealerId));

        // Temporary value
        dto.setVehicleViews(9900L);

        // Temporary chart data
        dto.setMonthlyViews(Arrays.asList(
                800, 950, 1070, 1200,
                1350, 1480, 1620, 1760,
                1910, 830, 970, 1100));

        dto.setMonthlyLeads(Arrays.asList(
                12, 19, 25, 32,
                12, 19, 25, 32,
                12, 19, 25, 32));

        return dto;
    }
}