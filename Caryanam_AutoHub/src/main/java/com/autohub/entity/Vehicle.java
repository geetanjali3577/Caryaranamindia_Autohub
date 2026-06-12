package com.autohub.entity;

import com.autohub.enums.VehicleStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle-info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String vehicleId;

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

    @Column(length = 5000)
    private String vehicleDescription;

    private Boolean financeAvailability;

    @Enumerated(EnumType.STRING)
    private VehicleStatus vehicleStatus;

    private String dealerContactName;
    private String dealerContactNumber;
    private String dealerContactEmail;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private VehicleStatus status;

    @ManyToOne
    @JoinColumn(name = "dealer_id")
    private Dealer dealer;
}
