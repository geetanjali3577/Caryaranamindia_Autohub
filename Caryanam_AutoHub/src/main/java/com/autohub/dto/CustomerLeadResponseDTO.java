package com.autohub.dto;

import com.autohub.enums.CustomerLeadStatus;
import com.autohub.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerLeadResponseDTO {

    private Long id;

    private String customerName;

    private String customerMobile;

    private String customerEmail;

    private String customerCity;

    private LocalDateTime enquiryDate;

    private String customerPassword;

    private LocalDateTime accountCreateAt;

    private Role role;

    private CustomerLeadStatus leadStatus;

    private String vehicleName;

    private Long dealer;
}
