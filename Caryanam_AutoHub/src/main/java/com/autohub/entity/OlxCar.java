package com.autohub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "olx_car")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OlxCar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sourceId;

    private String brand;

    private String carName;

    private String variantName;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    private String fuelType;

    private Long kmDriven;

    private BigDecimal price;

    private Integer noOfOwners;

    private Integer modelYear;

    private String location;

    private Long subLocalityId;

    @Column(columnDefinition = "TEXT")
    private String sourceUrl;

    private String dealerName;

    private String contactNo;

    private LocalDateTime createdAt;

    @OneToMany(
            mappedBy = "car",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<OlxCarImage> images = new ArrayList<>();
}