package com.autohub.serviceImpl;

import com.autohub.configuration.ZipExtractor;
import com.autohub.dto.CarImageResponse;
import com.autohub.dto.CarResponse;
import com.autohub.entity.OlxCar;
import com.autohub.entity.OlxCarImage;
import com.autohub.repository.OlxCarRepository;
import com.autohub.service.OlxImportService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OlxImportServiceImpl implements OlxImportService {

    private final OlxCarRepository carRepository;
    private final ZipExtractor zipExtractor;

    @Override
    public void importData(MultipartFile excel,
                           MultipartFile zip) throws Exception {

        String uploadPath = "uploads/olx";

        zipExtractor.unzip(zip, uploadPath);

        try (Workbook workbook =
                     WorkbookFactory.create(excel.getInputStream())) {

            Sheet sheet = workbook.getSheetAt(0);

            DataFormatter formatter = new DataFormatter();

            for (int rowNum = 1;
                 rowNum <= sheet.getLastRowNum();
                 rowNum++) {

                Row row = sheet.getRow(rowNum);

                if (isRowEmpty(row)) {
                    continue;
                }

                String carName =
                        getStringValue(formatter, row, 1);

                if (carName.isBlank()) {
                    continue;
                }

                OlxCar car = new OlxCar();

                car.setLocation(
                        getStringValue(formatter, row, 0));

                car.setCarName(carName);

                car.setVariantName(
                        getStringValue(formatter, row, 2));

                car.setBrand(
                        getStringValue(formatter, row, 3));

                car.setDescription(
                        getStringValue(formatter, row, 4));

                car.setFuelType(
                        getStringValue(formatter, row, 5));

                car.setKmDriven(
                        getLongValue(formatter, row, 6));

                car.setPrice(
                        BigDecimal.valueOf(
                                getDoubleValue(formatter, row, 7)));

                car.setNoOfOwners(
                        getLongValue(formatter, row, 8).intValue());

                car.setModelYear(
                        getLongValue(formatter, row, 9).intValue());

                car.setSourceId(
                        getLongValue(formatter, row, 10));

                car.setSubLocalityId(
                        getLongValue(formatter, row, 11));

                car.setSourceUrl(
                        getStringValue(formatter, row, 30));

                car.setDealerName(
                        getStringValue(formatter, row, 31));

                car.setContactNo(
                        getStringValue(formatter, row, 32));

                car.setCreatedAt(LocalDateTime.now());

                int imageCount = 0;

                for (int col = 13; col <= 30; col++) {

                    String imageName =
                            "row_" + rowNum +
                                    "_col_" + col + ".jpg";

                    File imageFile =
                            new File(uploadPath
                                    + "/Images_Processed/"
                                    + imageName);

                    if (imageFile.exists()) {

                        OlxCarImage image =
                                new OlxCarImage();

                        image.setImageUrl(
                                "/uploads/olx/Images_Processed/"
                                        + imageName);

                        image.setCar(car);

                        car.getImages().add(image);

                        imageCount++;
                    }
                }

                carRepository.save(car);

                System.out.println(
                        "Imported Row : "
                                + rowNum
                                + " | Car : "
                                + carName
                                + " | Images : "
                                + imageCount);
            }
        }
    }

    @Override
    public CarResponse getCar(Long id) {

        OlxCar car = carRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Car Not Found"));

        CarResponse response = new CarResponse();

        response.setId(car.getId());
        response.setBrand(car.getBrand());
        response.setCarName(car.getCarName());
        response.setPrice(car.getPrice());

        List<CarImageResponse> images =
                car.getImages()
                        .stream()
                        .map(image -> {

                            CarImageResponse dto =
                                    new CarImageResponse();

                            dto.setId(image.getId());
                            dto.setImageUrl(
                                    image.getImageUrl());

                            return dto;

                        }).toList();

        response.setImages(images);

        return response;
    }

    private boolean isRowEmpty(Row row) {

        if (row == null) {
            return true;
        }

        DataFormatter formatter =
                new DataFormatter();

        for (int i = row.getFirstCellNum();
             i < row.getLastCellNum();
             i++) {

            Cell cell = row.getCell(i);

            if (cell != null &&
                    !formatter.formatCellValue(cell)
                            .trim()
                            .isEmpty()) {

                return false;
            }
        }

        return true;
    }

    private String getStringValue(
            DataFormatter formatter,
            Row row,
            int index) {

        Cell cell = row.getCell(index);

        if (cell == null) {
            return "";
        }

        return formatter
                .formatCellValue(cell)
                .trim();
    }

    private Long getLongValue(
            DataFormatter formatter,
            Row row,
            int index) {

        try {

            String value =
                    getStringValue(
                            formatter,
                            row,
                            index);

            if (value.isBlank()) {
                return 0L;
            }

            return Long.parseLong(
                    value.replace(".0", ""));

        } catch (Exception e) {
            return 0L;
        }
    }

    private Double getDoubleValue(
            DataFormatter formatter,
            Row row,
            int index) {

        try {

            String value =
                    getStringValue(
                            formatter,
                            row,
                            index);

            if (value.isBlank()) {
                return 0D;
            }

            value = value.replace(",", "");

            return Double.parseDouble(value);

        } catch (Exception e) {
            return 0D;
        }
    }


    @Override
    public List<CarResponse> getAllCars() {

        List<OlxCar> cars = carRepository.findAll();

        return cars.stream()
                .map(car -> {

                    CarResponse response =
                            new CarResponse();

                    response.setId(car.getId());
                    response.setBrand(car.getBrand());
                    response.setCarName(car.getCarName());
                    response.setPrice(car.getPrice());

                    List<CarImageResponse> images =
                            car.getImages()
                                    .stream()
                                    .map(image -> {

                                        CarImageResponse dto =
                                                new CarImageResponse();

                                        dto.setId(image.getId());
                                        dto.setImageUrl(
                                                image.getImageUrl());

                                        return dto;

                                    }).toList();

                    response.setImages(images);

                    return response;

                }).toList();
    }
}