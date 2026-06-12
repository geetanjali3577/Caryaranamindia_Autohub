package com.autohub.dto;

import lombok.Data;

import java.util.List;

@Data
public class DashboardResponseDTO {

    private String dealerName;

    private Long totalVehicles;
    private Long featuredVehicles;
    private Long totalLeads;
    private Long vehicleViews;

    private List<Integer> monthlyViews;
    private List<Integer> monthlyLeads;
}
