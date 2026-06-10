package com.autohub.entity;

import com.autohub.enums.VehicleStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vehicleId;

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

    @Column(length = 5000,nullable = false)
    private String vehicleDescription;

    private Boolean financeAvailability;

    private Boolean featured;

    private String dealerContactName;

    private String dealerContactNumber;

    private String dealerContactEmail;

    private String status= String.valueOf(VehicleStatus.ACTIVE);

    @ElementCollection
    @CollectionTable(
            name = "vehicle_images",
            joinColumns = @JoinColumn(name = "vehicle_id")
    )
    @Column(name = "image_path")
    private List<String> imagePaths;

    private String videoPath;
}
