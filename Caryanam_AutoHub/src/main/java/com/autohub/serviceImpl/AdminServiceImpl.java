package com.autohub.serviceImpl;

import com.autohub.dto.*;
import com.autohub.entity.CustomerLead;
import com.autohub.entity.Dealer;
import com.autohub.exception.ResourceNotFoundException;
import com.autohub.repository.AdminRepository;
import com.autohub.repository.CustomerLeadRepository;
import com.autohub.repository.DealerRepository;
import com.autohub.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final DealerRepository dealerRepository;


    private final CustomerLeadRepository customerLeadRepository;


    //All dealer
    @Override
    public List<DealerResponseDTO> allDealer() {

        List<Dealer> all = dealerRepository.findAll();

        if (all.isEmpty()) {
            throw new ResourceNotFoundException("Dealer has no vehicles");
        }

        return all.stream()
                .map(dealer -> DealerResponseDTO.builder()
                        .id(dealer.getId())
                        .businessName(dealer.getBusinessName())
                        .ownerName(dealer.getOwnerName())
                        .gstNumber(dealer.getGstNumber())
                        .yearsInBusiness(dealer.getYearsInBusiness())
                        .mobile(dealer.getMobile())
                        .whatsapp(dealer.getWhatsapp())
                        .email(dealer.getEmail())
                        .address(dealer.getAddress())
                        .city(dealer.getCity())
                        .state(dealer.getState())
                        .pinCode(dealer.getPinCode())
                        .dealerLogo(dealer.getDealerLogo())
                        .showroomImage(dealer.getShowroomImage())
                        .dealerAccountStatus(dealer.getDealerAccountStatus())
                        .createdAt(dealer.getCreatedAt())
                        .build())
                .toList();
    }

    //Dealer Count
    @Override
    public DealerCountResponseDTO getTotalDealerCount() {

        return DealerCountResponseDTO.builder()
                .totalDealers(dealerRepository.count())
                .build();
    }


    @Override
    public List<AllCustomerLeadResponseDTO> getAllCustomerLeads() {

        List<CustomerLead> allLeads = customerLeadRepository.findAll();

        return allLeads.stream()
                .map(lead -> AllCustomerLeadResponseDTO.builder()
                        .id(lead.getId())
                        .customerName(lead.getCustomerName())
                        .customerMobile(lead.getCustomerMobile())
                        .customerCity(lead.getCustomerCity())
                        .leadStatus(lead.getLeadStatus())
                        .enquiryDate(lead.getEnquiryDate())
                        .vehicleName(lead.getVehicle().getVariant())
                        .build())
                .toList();
    }

}

