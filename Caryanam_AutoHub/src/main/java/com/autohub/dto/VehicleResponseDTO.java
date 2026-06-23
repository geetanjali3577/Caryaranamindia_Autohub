package com.autohub.dto;

import com.autohub.enums.VehicleStatus;
import com.autohub.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleResponseDTO {

    private Long id;

    private Long dealerId;

    private String brand;

    private String model;

    private String variant;

    private Integer registrationYear;

    private Double askingPrice;

    private Long kilometerDriven;

    private String fuelType;

    private String transmission;

    private String ownershipDetails;

    private String insuranceStatus;

    private String city;

    private String vehicleDescription;

    private VehicleStatus vehicleStatus;

    private VehicleType vehicleType;

    private LocalDateTime createdAt;

    private String dealerContactName;

    private String dealerContactNumber;

    private  String dealerContactEmail;

    private String dealerWhatsappNumber;

    private String dealerBusinessName;

    private boolean isWishList;

    private List<String> images;

    private List<String> videos;

}