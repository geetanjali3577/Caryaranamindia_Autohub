package com.autohub.service;

import com.autohub.dto.LeadRequestDTO;
import com.autohub.dto.LeadResponseDTO;
import com.autohub.dto.LeadStatusRequestDTO;
import com.autohub.dto.MonthlyLeadAnalyticsDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LeadService {

    LeadResponseDTO createLead(Long vehicleId,LeadRequestDTO leadRequestDTO);

    List<LeadResponseDTO> getDealerLeads(Long dealerId);

    LeadResponseDTO updateLeadStatus(Long leadId, LeadStatusRequestDTO requestDTO);

    List<MonthlyLeadAnalyticsDTO> getLeadAnalytics(Long dealerId);

}
