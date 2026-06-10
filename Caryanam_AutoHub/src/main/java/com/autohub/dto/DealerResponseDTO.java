package com.autohub.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DealerResponseDTO {

    private Long dealerId;

    private String businessName;
    private String ownerName;
    private String gstNumber;
    private Integer yearsInBusiness;

    private String mobile;
    private String whatsapp;
    private String email;
    private String password;

    private String address;
    private String city;
    private String state;
    private String pinCode;

    private String dealerLogo;
    private String showroomImage;

    private LocalDateTime createdAt;

}
