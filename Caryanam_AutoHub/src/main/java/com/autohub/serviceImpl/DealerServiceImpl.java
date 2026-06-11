package com.autohub.serviceImpl;

import com.autohub.dto.*;
import com.autohub.emailservice.EmailService;
import com.autohub.entity.Dealer;
import com.autohub.enums.DealerStatus;
import com.autohub.enums.Role;
import com.autohub.repository.DealerRepository;
import com.autohub.repository.UserRepository;
import com.autohub.service.DealerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DealerServiceImpl implements DealerService {

    @Autowired
    private DealerRepository dealerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;
    @Override
    public DealerResponseDTO registerDealer(DealerRegisterDTO dto) {

        if (dealerRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email Already Exists");
        }
        if (dealerRepository.existsByMobile(dto.getMobile())) {
            throw new RuntimeException("Mobile Number Already Exists");
        }


        Dealer dealer = new Dealer();
        dealer.setDealerCode(dto.getDealerId());
        dealer.setBusinessName(dto.getBusinessName());
        dealer.setOwnerName(dto.getOwnerName());
        dealer.setGstNumber(dto.getGstNumber());
        dealer.setYearsInBusiness(dto.getYearsInBusiness());

        dealer.setMobile(dto.getMobile());
        dealer.setWhatsapp(dto.getWhatsapp());
        dealer.setEmail(dto.getEmail());
        dealer.setPassword(passwordEncoder.encode(dto.getPassword()));
        dealer.setCreatedAt(LocalDateTime.now());
        dealer.setAddress(dto.getAddress());
        dealer.setCity(dto.getCity());
        dealer.setState(dto.getState());
        dealer.setPinCode(dto.getPinCode());

        dealer.setDealerLogo(dto.getDealerLogo());
        dealer.setShowroomImage(dto.getShowroomImage());


        dealer.setRole(Role.DEALER);
        dealer.setStatus(DealerStatus.ACTIVE);

        Dealer savedDealer = dealerRepository.save(dealer);

        return DealerResponseDTO.builder()
                .id(dealer.getId())
                .dealerId(dealer.getDealerCode())
                .businessName(dealer.getBusinessName())
                .ownerName(dealer.getOwnerName())
                .gstNumber(dealer.getGstNumber())
                .yearsInBusiness(dealer.getYearsInBusiness())
                .mobile(dealer.getMobile())
                .whatsapp(dealer.getWhatsapp())
                .dealerLogo(dealer.getDealerLogo())
                .showroomImage(dealer.getShowroomImage())
                .email(dealer.getEmail())
                .password(dealer.getPassword())
                .address(dealer.getAddress())
                .city(dealer.getCity())
                .state(dealer.getState())
                .pinCode(dealer.getPinCode())
                .createdAt(dealer.getCreatedAt())
                .build();
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
                        "Caryanam Finserv Team";

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
                        "Caryanam Finserv Team";

        emailService.sendMail(
                dealer.getEmail(),
                subject,
                body
        );

        return "Password reset successfully";
    }
/*
    @Override
    public DealerResponseDTO updateDealer(Long id, DealerRegisterDTO dto) {

        Dealer dealer = dealerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dealer Not Found"));

        if (dto.getOwnerName() != null && !dto.getOwnerName().isEmpty()) {
            dealer.getOwnerName(dto.getOwnerName());
        }

        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            String newEmail = dto.getEmail().toLowerCase().trim();
            if (!dealer.getEmail().equalsIgnoreCase(newEmail) && dealerRepository.existsByEmail(newEmail)) {
                throw new RuntimeException("Email Already Exists");
            }
            dealer.setEmail(newEmail);
        }

        if (dto.getMobile() != null && !dto.getMobile().isEmpty()) {
            if (!dealer.getMobile().equals(dto.getMobile()) && dealerRepository.existsByMobile(dto.getMobile())) {
                throw new RuntimeException("Mobile Number Already Exists");
            }
            dealer.setMobile(dto.getMobile());
        }

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            dealer.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        Dealer savedDealer = dealerRepository.save(dealer);
        return DealerResponseDTO.builder()
                .dealerId(savedDealer.getDealerId())
                .ownerName(savedDealer.getOwnerName()
                .email(savedDealer.getEmail())
                .mobileNumber(savedDealer.getMobile())
                .role(savedDealer.getRole().name())
                .createdAt(savedDealer.getCreatedAt())
                .build();
    }

    @Override
    public List<DealerResponseDTO> getAllDealers() {
        List<Dealer> dealers = dealerRepository.findAll();
        return dealers.stream().map(d -> DealerResponseDTO.builder()
                .dealerId(d.getDealerId())
                .dealerCode(d.getDealerCode())
                .fullName(d.getFullName())
                .email(d.getEmail())
                .mobileNumber(d.getMobileNumber())
                .role(d.getRole().name())
                .createdAt(d.getCreatedAt())
                .build()).toList();
    }

    @Override
    public DealerResponseDTO searchByDealerCode(String dealerCode) {

        Dealer dealer = dealerRepository
                .findByDealerCode(dealerCode)
                .orElseThrow(() ->
                        new RuntimeException("Dealer not found"));

        return mapToDealerResponseDTO(dealer);
    }

    private DealerResponseDTO mapToDealerResponseDTO(Dealer dealer) {

        return DealerResponseDTO.builder()
                .dealerId(dealer.getDealerId())
                .dealerCode(dealer.getDealerCode())
                .fullName(dealer.getFullName())
                .email(dealer.getEmail())
                .mobileNumber(dealer.getMobileNumber())
                .createdAt(dealer.getCreatedAt())
                .build();
    }
*/

    @Override
    public List<DealerSubscriptionResponseDTO> getSubscriptions() {

        return dealerRepository.findAll()
                .stream()
                .map(dealer -> {

                    DealerSubscriptionResponseDTO dto =
                            new DealerSubscriptionResponseDTO();

                    dto.setId((dealer.getId()));

                    dto.setDealerName(dealer.getBusinessName());

                    dto.setDealerId(dealer.getDealerCode());

                    dto.setSubscriptionActive(
                            dealer.getSubscriptionActive());

                    dto.setSubscriptionPlan(
                            dealer.getSubscriptionPlan());

                    return dto;

                }).toList();
    }
}