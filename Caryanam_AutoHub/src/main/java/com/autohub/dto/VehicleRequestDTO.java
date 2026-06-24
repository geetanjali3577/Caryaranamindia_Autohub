package com.autohub.dto;

import com.autohub.enums.InsuranceStatus;
import com.autohub.enums.VehicleType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VehicleRequestDTO {

    @NotBlank(message = "Brand Name is Required")
    private String brand;

    @NotBlank(message = "Model Name is Required")
    private String model;

    @NotBlank(message = "Variant Name is Required")
    private String variant;

    @NotNull(message = "Registration Year is Required")
    private Integer registrationYear;

    @NotNull(message = "Asking Price is Required")
    private Double askingPrice;

    @NotNull(message = "Kilometer Driven is Required")
    private Long kilometerDriven;

    @NotBlank(message = "Fuel Type is Required")
    private String fuelType;

    @NotBlank(message = "Transmission is Required")
    private String transmission;

    @NotBlank(message = "Ownership Details is Required")
    private String ownershipDetails;

    @NotBlank(message = "Insurance Status is Required")
    private InsuranceStatus insuranceStatus;

    @NotBlank(message = "City is Required")
    private String city;

    @NotBlank(message = "RTO Information is Required")
    private String rtoInformation;

    @NotNull(message = "Finance Availability is Required")
    private Boolean financeAvailability;

    @NotBlank(message = "Vehicle Description is Required")
    private String vehicleDescription;

    @NotNull(message = "Vehicle Type is Required")
    private VehicleType vehicleType;




}