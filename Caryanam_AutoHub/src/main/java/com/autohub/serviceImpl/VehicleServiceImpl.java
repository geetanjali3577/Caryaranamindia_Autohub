package com.autohub.service.impl;

import com.autohub.dto.VehicleRequestDTO;
import com.autohub.entity.Vehicle;
import com.autohub.repository.VehicleRepository;
import com.autohub.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    private static final String IMAGE_DIR = "uploads/images/";
    private static final String VIDEO_DIR = "uploads/videos/";

    @Override
    public String addVehicle(
            VehicleRequestDTO dto,
            List<MultipartFile> images,
            MultipartFile video
    ) throws IOException {

        if(images == null || images.size() < 10){
            throw new RuntimeException(
                    "Minimum 10 images required"
            );
        }

        Files.createDirectories(Paths.get(IMAGE_DIR));
        Files.createDirectories(Paths.get(VIDEO_DIR));

        List<String> imagePaths = new ArrayList<>();

        for(MultipartFile image : images){

            String imageName =
                    UUID.randomUUID() + "_" +
                            image.getOriginalFilename();

            Path imagePath =
                    Paths.get(IMAGE_DIR, imageName);

            Files.copy(
                    image.getInputStream(),
                    imagePath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            imagePaths.add(imagePath.toString());
        }

        String videoPathString = null;

        if(video != null && !video.isEmpty()){

            String videoName =
                    UUID.randomUUID() + "_" +
                            video.getOriginalFilename();

            Path videoPath =
                    Paths.get(VIDEO_DIR, videoName);

            Files.copy(
                    video.getInputStream(),
                    videoPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            videoPathString = videoPath.toString();
        }

        Vehicle vehicle = Vehicle.builder()
                .vehicleId(
                        "VH" + System.currentTimeMillis()
                )
                .dealerId(dto.getDealerId())
                .brand(dto.getBrand())
                .model(dto.getModel())
                .variant(dto.getVariant())
                .manufacturingYear(dto.getManufacturingYear())
                .registrationYear(dto.getRegistrationYear())
                .fuelType(dto.getFuelType())
                .transmission(dto.getTransmission())
                .kilometerDriven(dto.getKilometerDriven())
                .ownershipDetails(dto.getOwnershipDetails())
                .insuranceStatus(dto.getInsuranceStatus())
                .rtoInformation(dto.getRtoInformation())
                .askingPrice(dto.getAskingPrice())
                .vehicleDescription(dto.getVehicleDescription())
                .financeAvailability(dto.getFinanceAvailability())
                .featured(dto.getFeatured())
                .dealerContactName(dto.getDealerContactName())
                .dealerContactNumber(dto.getDealerContactNumber())
                .dealerContactEmail(dto.getDealerContactEmail())
                .status("ACTIVE")
                .imagePaths(imagePaths)
                .videoPath(videoPathString)
                .build();

        vehicleRepository.save(vehicle);

        return "Vehicle Added Successfully";
    }


}