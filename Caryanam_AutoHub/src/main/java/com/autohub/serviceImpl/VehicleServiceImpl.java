package com.autohub.serviceImpl;

import com.autohub.dto.VehicleRequestDTO;
import com.autohub.dto.VehicleResponseDTO;
import com.autohub.dto.VehicleStatusRequestDTO;
import com.autohub.entity.*;
import com.autohub.enums.*;
import com.autohub.exception.ResourceNotFoundException;
import com.autohub.repository.*;
import com.autohub.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

   private final VehicleRepository vehicleRepository;
   private final DealerRepository dealerRepository;
   private final ModelMapper modelMapper;
   private final VehicleMediaRepository mediaRepository;
   private final CustomerLeadRepository leadRepository;
   private final PaymentRepository paymentRepository;
   private final VehicleViewRepository vehicleViewRepository;


    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${server.port}")
    private String port;

    @Override
    public VehicleResponseDTO addVehicleWithData(VehicleRequestDTO vehicleRequestDTO, List<MultipartFile> images, List<MultipartFile> videos, Long dealerId) throws IOException {

        Dealer dealer = dealerRepository.findById(dealerId)
                .orElseThrow(() ->
                        new RuntimeException("Dealer not found"));

        // Dealer account status Check
        if (dealer.getDealerAccountStatus() != DealerStatus.APPROVED) {
            throw new RuntimeException("Your account is pending for admin approval. You cannot add vehicles until approval.");
        }

        // Dealer subscription Check
//        if (dealer.getSubscriptionActive() == null ||
//                !dealer.getSubscriptionActive()) {
//            throw new RuntimeException("Please purchase subscription before adding vehicles");
//        }

        //Check subscription plan
        Payment payment = paymentRepository.findTopByDealerIdOrderByPaymentIdDesc(dealerId)
                .orElseThrow(() ->
                        new RuntimeException("You don't have any subscription plan. Please purchase subscription before adding vehicles"));

        if (payment.getPaymentStatus() == PaymentStatus.PENDING) {

            throw new RuntimeException(
                    "Your subscription plan is waiting for admin approval.");
        }

        // Dealer subscription expiration Check
        if (dealer.getSubscriptionEndDate() != null &&dealer.getSubscriptionEndDate().isBefore(LocalDateTime.now())) {

            throw new RuntimeException("Subscription expired. Please renew subscription");
        }

        // Vehicle Limit Check
        Long vehicleCount = vehicleRepository.countByDealer_Id(dealerId);

        if (dealer.getSubscriptionPlan() != SubscriptionPlan.PREMIUM) {

            int vehicleLimit = dealer.getSubscriptionPlan().getVehicleLimit();

            if (vehicleCount >= vehicleLimit) {
                throw new RuntimeException(
                        "Vehicle limit exceeded. Your "
                                + dealer.getSubscriptionPlan()
                                + " plan allows only "
                                + vehicleLimit
                                + " vehicles.");
            }
        }

        //Minimum 10 image required to add vehicle
        if (images == null || images.size() < 10) {
            throw new RuntimeException(
                    "Minimum 10 images are required");
        }

        //Video required to add vehicle
        if (videos == null || videos.isEmpty()) {
            throw new RuntimeException(
                    "Minimum 1 video is required");
        }

        for (MultipartFile image : images) {

            String contentType = image.getContentType();

            if (contentType == null ||
                    !(contentType.equalsIgnoreCase("image/jpeg")
                            || contentType.equalsIgnoreCase("image/jpg")
                            || contentType.equalsIgnoreCase("image/png"))) {

                throw new RuntimeException(
                        "Only JPG, JPEG and PNG images are allowed");
            }
        }

        // Save Vehicle
        Vehicle vehicle = Vehicle.builder()
                .dealer(dealer)
                .brand(vehicleRequestDTO.getBrand())
                .model(vehicleRequestDTO.getModel())
                .variant(vehicleRequestDTO.getVariant())
                .registrationYear(vehicleRequestDTO.getRegistrationYear())
                .fuelType(vehicleRequestDTO.getFuelType())
                .transmission(vehicleRequestDTO.getTransmission())
                .kilometerDriven(vehicleRequestDTO.getKilometerDriven())
                .ownershipDetails(vehicleRequestDTO.getOwnershipDetails())
                .insuranceStatus(vehicleRequestDTO.getInsuranceStatus())
                .askingPrice(vehicleRequestDTO.getAskingPrice())
                .vehicleDescription(vehicleRequestDTO.getVehicleDescription())
                .city(vehicleRequestDTO.getCity())
                .dealerContactName(dealer.getOwnerName())
                .dealerContactNumber(dealer.getMobile())
                .dealerWhatsappNumber(dealer.getWhatsapp())
                .dealerBusinessName(dealer.getBusinessName())
                .dealerContactEmail(dealer.getEmail())
                .vehicleStatus(VehicleStatus.ACTIVE)
                .vehicleType(vehicleRequestDTO.getVehicleType())
                .createdAt(LocalDateTime.now())
                .build();

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        // IMAGE UPLOAD

        if (images != null && !images.isEmpty()) {

            for (MultipartFile file : images) {

                String imageFolder =
                        uploadDir +
                                "/dealer_" + dealerId +
                                "/vehicle_" + savedVehicle.getId() +
                                "/images";

                File folder = new File(imageFolder);

                if (!folder.exists()) {
                    folder.mkdirs();
                }

                String fileName =
                        System.currentTimeMillis()
                                + "_"
                                + file.getOriginalFilename();

                Path filePath =
                        Paths.get(imageFolder, fileName);

                Files.copy(
                        file.getInputStream(),
                        filePath,
                        StandardCopyOption.REPLACE_EXISTING);

                VehicleMedia media =
                        VehicleMedia.builder()
                                .fileName(fileName)
                                .fileType(file.getContentType())
                                .filePath(filePath.toString())
                                .mediaType("IMAGE")
                                .vehicle(savedVehicle)
                                .uploadedAt(LocalDateTime.now())
                                .build();

                mediaRepository.save(media);
            }
        }

        // VIDEO UPLOAD

        if (videos != null && !videos.isEmpty()) {

            for (MultipartFile file : videos) {

                String videoFolder =
                        uploadDir +
                                "/dealer_" + dealerId +
                                "/vehicle_" + savedVehicle.getId() +
                                "/videos";

                File folder = new File(videoFolder);

                if (!folder.exists()) {
                    folder.mkdirs();
                }

                String fileName =
                        System.currentTimeMillis()
                                + "_"
                                + file.getOriginalFilename();

                Path filePath =
                        Paths.get(videoFolder, fileName);

                Files.copy(
                        file.getInputStream(),
                        filePath,
                        StandardCopyOption.REPLACE_EXISTING);

                VehicleMedia media =
                        VehicleMedia.builder()
                                .fileName(fileName)
                                .fileType(file.getContentType())
                                .filePath(filePath.toString())
                                .mediaType("VIDEO")
                                .uploadedAt(LocalDateTime.now())
                                .vehicle(savedVehicle)
                                .build();

                mediaRepository.save(media);
            }
        }

        List<VehicleMedia> mediaList =
                mediaRepository.findByVehicleId(savedVehicle.getId());

        List<String> image = mediaList.stream()
                .filter(media -> "IMAGE".equalsIgnoreCase(media.getMediaType()))
                .map(media -> "http://localhost:"+port + "/" +
                        media.getFilePath().replace("\\", "/"))
                .toList();

        List<String> video = mediaList.stream()
                .filter(media -> "VIDEO".equalsIgnoreCase(media.getMediaType()))
                .map(media -> "http://localhost:"+port + "/" +
                        media.getFilePath().replace("\\", "/"))
                .toList();

        System.out.println("Media Count = " + mediaList.size());

        return VehicleResponseDTO.builder()
                .id(savedVehicle.getId())
                .dealerId(savedVehicle.getDealer().getId())
                .brand(savedVehicle.getBrand())
                .model(savedVehicle.getModel())
                .variant(savedVehicle.getVariant())
                .registrationYear(savedVehicle.getRegistrationYear())
                .fuelType(savedVehicle.getFuelType())
                .transmission(savedVehicle.getTransmission())
                .kilometerDriven(savedVehicle.getKilometerDriven())
                .ownershipDetails(savedVehicle.getOwnershipDetails())
                .insuranceStatus(savedVehicle.getInsuranceStatus())
                .askingPrice(savedVehicle.getAskingPrice())
                .vehicleDescription(savedVehicle.getVehicleDescription())
                .city(savedVehicle.getCity())
                .dealerContactName(vehicle.getDealer().getOwnerName())
                .dealerContactNumber(vehicle.getDealer().getMobile())
                .dealerWhatsappNumber(vehicle.getDealer().getWhatsapp())
                .dealerBusinessName(vehicle.getDealer().getBusinessName())
                .dealerContactEmail(vehicle.getDealer().getEmail())
                .vehicleStatus(savedVehicle.getVehicleStatus())
                .vehicleType(savedVehicle.getVehicleType())
                .createdAt(savedVehicle.getCreatedAt())
                .images(image)
                .videos(video)
                .build();

    }


    @Override
    public VehicleResponseDTO updateVehicle(Long id, VehicleRequestDTO request) {

        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() ->
                        new ResourceNotFoundException("Vehicle not found with id: " + id));

        vehicle.setBrand(request.getBrand());
        vehicle.setModel(request.getModel());
        vehicle.setVariant(request.getVariant());
        vehicle.setRegistrationYear(request.getRegistrationYear());
        vehicle.setFuelType(request.getFuelType());
        vehicle.setTransmission(request.getTransmission());
        vehicle.setKilometerDriven(request.getKilometerDriven());
        vehicle.setOwnershipDetails(request.getOwnershipDetails());
        vehicle.setInsuranceStatus(request.getInsuranceStatus());
        vehicle.setAskingPrice(request.getAskingPrice());
        vehicle.setVehicleDescription(request.getVehicleDescription());

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);

        return modelMapper.map(updatedVehicle, VehicleResponseDTO.class);
    }


    @Override
    public VehicleResponseDTO updateVehicleStatus(Long id,VehicleStatusRequestDTO request) {

        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vehicle not found"));

        if (!request.getStatus().equalsIgnoreCase("ACTIVE")
                && !request.getStatus().equalsIgnoreCase("INACTIVE") && !request.getStatus().equalsIgnoreCase("FEATURED")) {

            throw new RuntimeException("Status must be ACTIVE or INACTIVE");
        }

        vehicle.setVehicleStatus(VehicleStatus.valueOf(request.getStatus().toUpperCase()));

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);

        return VehicleResponseDTO.builder()
                .id(updatedVehicle.getId())
                .vehicleStatus(VehicleStatus.valueOf(String.valueOf(updatedVehicle.getVehicleStatus())))
                .build();
    }

    @Override
    @Transactional
    public void deleteVehicle(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Vehicle not found with id: " + id));

        leadRepository.deleteLeadsByVehicleId(vehicle.getId());

        vehicleViewRepository.deleteByVehicleId(vehicle.getId());

        vehicleRepository.deleteById(vehicle.getId());

    }


    @Override
    public List<VehicleResponseDTO> getAllVehicleByDealerId(Long dealerId) {

        List<Vehicle> vehicles = vehicleRepository.findByDealerId(dealerId);

        if (vehicles.isEmpty()) {
            throw new ResourceNotFoundException("Dealer has no vehicles");
        }

        return vehicles.stream()
                .map(vehicle -> VehicleResponseDTO.builder()
                        .id(vehicle.getId())
                        .dealerId(vehicle.getDealer().getId())
                        .brand(vehicle.getBrand())
                        .model(vehicle.getModel())
                        .variant(vehicle.getVariant())
                        .registrationYear(vehicle.getRegistrationYear())
                        .askingPrice(vehicle.getAskingPrice())
                        .kilometerDriven(vehicle.getKilometerDriven())
                        .fuelType(vehicle.getFuelType())
                        .transmission(vehicle.getTransmission())
                        .ownershipDetails(vehicle.getOwnershipDetails())
                        .insuranceStatus(vehicle.getInsuranceStatus())
                        .vehicleDescription(vehicle.getVehicleDescription())
                        .city(vehicle.getCity())
                        .dealerContactName(vehicle.getDealer().getOwnerName())
                        .dealerContactNumber(vehicle.getDealer().getMobile())
                        .dealerWhatsappNumber(vehicle.getDealer().getWhatsapp())
                        .dealerBusinessName(vehicle.getDealer().getBusinessName())
                        .dealerContactEmail(vehicle.getDealer().getEmail())
                        .vehicleStatus(vehicle.getVehicleStatus())
                        .createdAt(vehicle.getCreatedAt())
                        .images(
                                vehicle.getMediaList() == null
                                        ? List.of()
                                        : vehicle.getMediaList().stream()
                                        .filter(media -> "IMAGE".equalsIgnoreCase(media.getMediaType()))
                                        //.map(VehicleMedia::getFilePath)
                                        .map(media -> "http://localhost:"+port+"/" +
                                                media.getFilePath().replace("\\", "/"))
                                        .toList()
                        )

                        .videos(
                                vehicle.getMediaList() == null
                                        ? List.of()
                                        : vehicle.getMediaList().stream()
                                        .filter(media -> "VIDEO".equalsIgnoreCase(media.getMediaType()))
                                        //.map(VehicleMedia::getFilePath)
                                        .map(media ->"http://localhost:"+port+ "/" +
                                                media.getFilePath().replace("\\", "/"))
                                        .toList()
                        )

                        .build())
                .toList();
    }



    @Override
    public VehicleResponseDTO getVehicleById(Long vehicleId) {

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vehicle not found with id : " + vehicleId));


        return VehicleResponseDTO.builder()
                .id(vehicle.getId())
                .dealerId(vehicle.getDealer().getId())
                .brand(vehicle.getBrand())
                .model(vehicle.getModel())
                .variant(vehicle.getVariant())
                .registrationYear(vehicle.getRegistrationYear())
                .askingPrice(vehicle.getAskingPrice())
                .kilometerDriven(vehicle.getKilometerDriven())
                .fuelType(vehicle.getFuelType())
                .transmission(vehicle.getTransmission())
                .ownershipDetails(vehicle.getOwnershipDetails())
                .insuranceStatus(vehicle.getInsuranceStatus())
                .vehicleDescription(vehicle.getVehicleDescription())
                .city(vehicle.getCity())
                .dealerContactName(vehicle.getDealer().getOwnerName())
                .dealerContactNumber(vehicle.getDealer().getMobile())
                .dealerWhatsappNumber(vehicle.getDealer().getWhatsapp())
                .dealerBusinessName(vehicle.getDealer().getBusinessName())
                .dealerContactEmail(vehicle.getDealer().getEmail())
                .vehicleStatus(vehicle.getVehicleStatus())
                .createdAt(vehicle.getCreatedAt())
                .images(
                        vehicle.getMediaList() == null
                                ? List.of()
                                : vehicle.getMediaList().stream()
                                .filter(media -> "IMAGE".equalsIgnoreCase(media.getMediaType()))
                                //.map(VehicleMedia::getFilePath)
                                .map(media -> "http://localhost:"+port+"/" +
                                        media.getFilePath().replace("\\", "/"))
                                .toList()
                )

                .videos(
                        vehicle.getMediaList() == null
                                ? List.of()
                                : vehicle.getMediaList().stream()
                                .filter(media -> "VIDEO".equalsIgnoreCase(media.getMediaType()))
                                //.map(VehicleMedia::getFilePath)
                                .map(media -> "http://localhost:"+port+"/" +
                                        media.getFilePath().replace("\\", "/"))
                                .toList()
                )
                .build();
    }

    @Override
    public Page<VehicleResponseDTO> getAllNonPremiumVehicle(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Vehicle> vehicles =
                vehicleRepository.findAllActiveAndFeaturedVehicles(
                        VehicleType.NON_PREMIUM,
                        pageable);

        if (vehicles.isEmpty()) {
            throw new ResourceNotFoundException("No vehicles found");
        }

        return vehicles.map(vehicle -> VehicleResponseDTO.builder()
                .id(vehicle.getId())
                .dealerId(vehicle.getDealer().getId())
                .brand(vehicle.getBrand())
                .model(vehicle.getModel())
                .variant(vehicle.getVariant())
                .registrationYear(vehicle.getRegistrationYear())
                .askingPrice(vehicle.getAskingPrice())
                .kilometerDriven(vehicle.getKilometerDriven())
                .fuelType(vehicle.getFuelType())
                .transmission(vehicle.getTransmission())
                .ownershipDetails(vehicle.getOwnershipDetails())
                .insuranceStatus(vehicle.getInsuranceStatus())
                .vehicleDescription(vehicle.getVehicleDescription())
                .city(vehicle.getCity())
                .dealerContactName(vehicle.getDealer().getOwnerName())
                .dealerContactNumber(vehicle.getDealer().getMobile())
                .dealerWhatsappNumber(vehicle.getDealer().getWhatsapp())
                .dealerBusinessName(vehicle.getDealer().getBusinessName())
                .dealerContactEmail(vehicle.getDealer().getEmail())
                .vehicleStatus(vehicle.getVehicleStatus())
                .vehicleType(vehicle.getVehicleType())
                .createdAt(vehicle.getCreatedAt())

                .images(
                        vehicle.getMediaList() == null
                                ? List.of()
                                : vehicle.getMediaList().stream()
                                .filter(media -> "IMAGE".equalsIgnoreCase(media.getMediaType()))
                                .map(media -> "http://localhost:" + port + "/" +
                                        media.getFilePath().replace("\\", "/"))
                                .toList()
                )

                .videos(
                        vehicle.getMediaList() == null
                                ? List.of()
                                : vehicle.getMediaList().stream()
                                .filter(media -> "VIDEO".equalsIgnoreCase(media.getMediaType()))
                                .map(media -> "http://localhost:" + port + "/" +
                                        media.getFilePath().replace("\\", "/"))
                                .toList()
                )
                .build());
    }


    @Override
    public Page<VehicleResponseDTO> getAllPremiumVehicle(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Vehicle> vehicles =
                vehicleRepository.findAllActiveAndFeaturedVehicles(
                        VehicleType.PREMIUM,
                        pageable);

        if (vehicles.isEmpty()) {
            throw new ResourceNotFoundException("No vehicles found");
        }

        return vehicles.map(vehicle -> VehicleResponseDTO.builder()
                .id(vehicle.getId())
                .dealerId(vehicle.getDealer().getId())
                .brand(vehicle.getBrand())
                .model(vehicle.getModel())
                .variant(vehicle.getVariant())
                .registrationYear(vehicle.getRegistrationYear())
                .askingPrice(vehicle.getAskingPrice())
                .kilometerDriven(vehicle.getKilometerDriven())
                .fuelType(vehicle.getFuelType())
                .transmission(vehicle.getTransmission())
                .ownershipDetails(vehicle.getOwnershipDetails())
                .insuranceStatus(vehicle.getInsuranceStatus())
                .vehicleDescription(vehicle.getVehicleDescription())
                .city(vehicle.getCity())
                .dealerContactName(vehicle.getDealer().getOwnerName())
                .dealerContactNumber(vehicle.getDealer().getMobile())
                .dealerWhatsappNumber(vehicle.getDealer().getWhatsapp())
                .dealerBusinessName(vehicle.getDealer().getBusinessName())
                .dealerContactEmail(vehicle.getDealer().getEmail())
                .vehicleStatus(vehicle.getVehicleStatus())
                .vehicleType(vehicle.getVehicleType())
                .createdAt(vehicle.getCreatedAt())

                .images(
                        vehicle.getMediaList() == null
                                ? List.of()
                                : vehicle.getMediaList().stream()
                                .filter(media -> "IMAGE".equalsIgnoreCase(media.getMediaType()))
                                .map(media -> "http://localhost:" + port + "/" +
                                        media.getFilePath().replace("\\", "/"))
                                .toList()
                )

                .videos(
                        vehicle.getMediaList() == null
                                ? List.of()
                                : vehicle.getMediaList().stream()
                                .filter(media -> "VIDEO".equalsIgnoreCase(media.getMediaType()))
                                .map(media -> "http://localhost:" + port + "/" +
                                        media.getFilePath().replace("\\", "/"))
                                .toList()
                )
                .build());
    }


    @Override
    public List<VehicleResponseDTO> getLatestFeaturedVehicles() {

        List<Vehicle> vehicles = vehicleRepository
                .findTop10ByVehicleStatusOrderByIdDesc(
                        VehicleStatus.FEATURED);

        return vehicles.stream()
                .map(vehicle -> VehicleResponseDTO.builder()
                        .id(vehicle.getId())
                        .dealerId(vehicle.getDealer().getId())
                        .brand(vehicle.getBrand())
                        .model(vehicle.getModel())
                        .variant(vehicle.getVariant())
                        .registrationYear(vehicle.getRegistrationYear())
                        .askingPrice(vehicle.getAskingPrice())
                        .kilometerDriven(vehicle.getKilometerDriven())
                        .fuelType(vehicle.getFuelType())
                        .transmission(vehicle.getTransmission())
                        .ownershipDetails(vehicle.getOwnershipDetails())
                        .insuranceStatus(vehicle.getInsuranceStatus())
                        .vehicleDescription(vehicle.getVehicleDescription())
                        .city(vehicle.getCity())
                        .dealerContactName(vehicle.getDealer().getOwnerName())
                        .dealerContactNumber(vehicle.getDealer().getMobile())
                        .dealerWhatsappNumber(vehicle.getDealer().getWhatsapp())
                        .dealerBusinessName(vehicle.getDealer().getBusinessName())
                        .dealerContactEmail(vehicle.getDealer().getEmail())
                        .vehicleStatus(vehicle.getVehicleStatus())
                        .createdAt(vehicle.getCreatedAt())
                        .images(
                                vehicle.getMediaList() == null
                                        ? List.of()
                                        : vehicle.getMediaList().stream()
                                        .filter(media -> "IMAGE".equalsIgnoreCase(media.getMediaType()))
                                        //.map(VehicleMedia::getFilePath)
                                        .map(media -> "http://localhost:"+port+"/" +
                                                media.getFilePath().replace("\\", "/"))
                                        .toList()
                        )

                        .videos(
                                vehicle.getMediaList() == null
                                        ? List.of()
                                        : vehicle.getMediaList().stream()
                                        .filter(media -> "VIDEO".equalsIgnoreCase(media.getMediaType()))
                                        //.map(VehicleMedia::getFilePath)
                                        .map(media ->"http://localhost:"+port+ "/" +
                                                media.getFilePath().replace("\\", "/"))
                                        .toList()
                        )

                        .build())
                .toList();
    }

    @Override
    public List<VehicleResponseDTO> getLatestVehicles() {
//
//        List<Vehicle> vehicles =
//                vehicleRepository.findLatestActiveAndFeaturedVehicles();

        List<Vehicle> vehicles =
                vehicleRepository.findTop10ByVehicleStatusInOrderByCreatedAtDesc(
                        List.of(VehicleStatus.ACTIVE,VehicleStatus.FEATURED)
                );

        return vehicles.stream()
                .filter(vehicle ->
                        vehicle.getVehicleStatus() == VehicleStatus.ACTIVE
                                || vehicle.getVehicleStatus() == VehicleStatus.FEATURED)
                .map(vehicle -> VehicleResponseDTO.builder()
                        .id(vehicle.getId())
                        .dealerId(vehicle.getDealer().getId())
                        .brand(vehicle.getBrand())
                        .model(vehicle.getModel())
                        .variant(vehicle.getVariant())
                        .registrationYear(vehicle.getRegistrationYear())
                        .askingPrice(vehicle.getAskingPrice())
                        .kilometerDriven(vehicle.getKilometerDriven())
                        .fuelType(vehicle.getFuelType())
                        .transmission(vehicle.getTransmission())
                        .ownershipDetails(vehicle.getOwnershipDetails())
                        .insuranceStatus(vehicle.getInsuranceStatus())
                        .vehicleDescription(vehicle.getVehicleDescription())
                        .city(vehicle.getCity())
                        .dealerContactName(vehicle.getDealer().getOwnerName())
                        .dealerContactNumber(vehicle.getDealer().getMobile())
                        .dealerWhatsappNumber(vehicle.getDealer().getWhatsapp())
                        .dealerBusinessName(vehicle.getDealer().getBusinessName())
                        .dealerContactEmail(vehicle.getDealer().getEmail())
                        .vehicleStatus(vehicle.getVehicleStatus())
                        .createdAt(vehicle.getCreatedAt())
                        .images(
                                vehicle.getMediaList() == null
                                        ? List.of()
                                        : vehicle.getMediaList().stream()
                                        .filter(media -> "IMAGE".equalsIgnoreCase(media.getMediaType()))
                                        //.map(VehicleMedia::getFilePath)
                                        .map(media -> "http://localhost:"+port+"/" +
                                                media.getFilePath().replace("\\", "/"))
                                        .toList()
                        )

                        .videos(
                                vehicle.getMediaList() == null
                                        ? List.of()
                                        : vehicle.getMediaList().stream()
                                        .filter(media -> "VIDEO".equalsIgnoreCase(media.getMediaType()))
                                        //.map(VehicleMedia::getFilePath)
                                        .map(media ->"http://localhost:"+port+ "/" +
                                                media.getFilePath().replace("\\", "/"))
                                        .toList()
                        )

                        .build())
                .toList();
    }

}
