package com.autohub.dto;

import com.autohub.entity.Dealer;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class VehicleRequestDTO {
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
        private String dealerContactName;
        private String dealerContactNumber;
        private String dealerContactEmail;


}