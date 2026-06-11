package com.autohub.serviceImpl;

import com.autohub.entity.Vehicle;
import com.autohub.entity.VehicleMedia;
import com.autohub.exception.ResourceNotFoundException;
import com.autohub.repository.VehicleMediaRepository;
import com.autohub.repository.VehicleRepository;
import com.autohub.service.VehicleMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleMediaServiceImpl implements VehicleMediaService {


        @Value("${file.upload-dir}")
        private String uploadDir;

        private final VehicleRepository vehicleRepository;
        private final VehicleMediaRepository mediaRepository;

    @Override
    public String uploadVehicleMedia(Long id, List<MultipartFile> images, MultipartFile video) throws IOException {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vehicle not found"));

        if (images == null || images.size() < 10) {
            throw new RuntimeException(
                    "Minimum 10 images required");
        }

        if (video == null || video.isEmpty()) {
            throw new RuntimeException(
                    "Video is required");
        }

        String imageFolder =
                uploadDir + "/images/" + id;

        String videoFolder =
                uploadDir + "/videos/" + id;

        Files.createDirectories(Paths.get(imageFolder));
        Files.createDirectories(Paths.get(videoFolder));

        // Save Images
        for (MultipartFile image : images) {

            String fileName =
                    UUID.randomUUID() + "_" +
                            image.getOriginalFilename();

            Path path =
                    Paths.get(imageFolder, fileName);

            Files.copy(
                    image.getInputStream(),
                    path,
                    StandardCopyOption.REPLACE_EXISTING);

            VehicleMedia media = VehicleMedia.builder()
                    .id(id)
                    .mediaType("IMAGE")
                    .fileName(fileName)
                    .filePath(path.toString())
                    .uploadedAt(LocalDateTime.now())
                    .build();

            mediaRepository.save(media);
        }

        // Save Video
        String videoName =
                UUID.randomUUID() + "_" +
                        video.getOriginalFilename();

        Path videoPath =
                Paths.get(videoFolder, videoName);

        Files.copy(
                video.getInputStream(),
                videoPath,
                StandardCopyOption.REPLACE_EXISTING);

        VehicleMedia videoMedia =
                VehicleMedia.builder()
                        .id(id)
                        .mediaType("VIDEO")
                        .fileName(videoName)
                        .filePath(videoPath.toString())
                        .uploadedAt(LocalDateTime.now())
                        .build();

        mediaRepository.save(videoMedia);

        return "Media Uploaded Successfully";
    }
}
