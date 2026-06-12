package com.autohub.dto;

import com.autohub.entity.Dealer;
import com.autohub.enums.VehicleStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleResponseDTO {

    private Long vehicleId;

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

    private LocalDateTime createdAt;

    private String dealerContactName;

    private String dealerContactNumber;

    private  String dealerContactEmail;

//    @ManyToOne
//    @JoinColumn(name = "dealer_id")
//    private Dealer dealer;
}