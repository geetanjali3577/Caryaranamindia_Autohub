package com.autohub.service;

import com.autohub.dto.LeadRequestDTO;
import com.autohub.dto.LeadResponseDTO;
import com.autohub.dto.LeadStatusRequestDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LeadService {

    LeadResponseDTO createLead(Long vehicleId, Long dealerId, LeadRequestDTO leadRequestDTO);

    List<LeadResponseDTO> getDealerLeads(Long dealerId);

    LeadResponseDTO updateLeadStatus(Long leadId, LeadStatusRequestDTO requestDTO);

}
