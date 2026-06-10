package com.autohub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class VehicleRequestDTO {

    private String dealerId;

    private String brand;

    private String model;

    private String variant;

    private Integer manufacturingYear;

    private Integer registrationYear;

    private String fuelType;

    private String transmission;

    private Integer kilometerDriven;

    private String ownershipDetails;

    private String insuranceStatus;

    private String rtoInformation;

    private BigDecimal askingPrice;

    private String vehicleDescription;

    private Boolean financeAvailability;

    private Boolean featured;

    private String dealerContactName;

    private String dealerContactNumber;

    private String dealerContactEmail;
}