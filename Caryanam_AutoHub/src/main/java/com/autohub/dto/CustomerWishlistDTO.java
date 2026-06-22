package com.autohub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerWishlistDTO {

    private Long vehicleId;
    private String vehicleName;
    private String brand;
    private Double price;
    private LocalDateTime addedAt;
}
