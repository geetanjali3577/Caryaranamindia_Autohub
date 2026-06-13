package com.autohub.dto;

import com.autohub.enums.LeadStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeadResponseDTO {

    private Long id;

    private String customerName;

    private String customerMobile;

    private String customerEmail;

    private String customerCity;

    private LocalDateTime enquiryDate;

    private LeadStatus leadStatus;

    private String vehicleName;

    private Long dealer;
}
