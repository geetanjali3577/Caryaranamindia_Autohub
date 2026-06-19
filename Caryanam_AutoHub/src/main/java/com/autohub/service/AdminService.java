package com.autohub.service;

import com.autohub.dto.AllCustomerLeadResponseDTO;
import com.autohub.dto.CustomerLeadResponseDTO;
import com.autohub.dto.DealerCountResponseDTO;
import com.autohub.dto.DealerResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {

    List<DealerResponseDTO> allDealer();

    DealerCountResponseDTO getTotalDealerCount();

    List<AllCustomerLeadResponseDTO> getAllCustomerLeads();

}
