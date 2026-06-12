package com.autohub.dto;

import com.autohub.enums.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleResponseDTO {
    private String vehicleId;
    private Long dealerId;
    private String brand;
    private String model;
    private String variant;

    private Integer manufacturingYear;
    private Integer registrationYear;

    private String fuelType;
    private String transmission;

    private Long kilometerDriven;

    private String ownershipDetails;
    private String insuranceStatus;
    private String rtoInformation;

    private Double askingPrice;

    private String vehicleDescription;

    private Boolean financeAvailability;
    private VehicleStatus vehicleStatus;
    private String dealerContactName;
    private String dealerContactNumber;
    private String dealerContactEmail; 
    private LocalDateTime createdAt;
}