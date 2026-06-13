package com.autohub.serviceImpl;

import com.autohub.dto.DealerResponseDTO;
import com.autohub.dto.LeadRequestDTO;
import com.autohub.dto.LeadResponseDTO;
import com.autohub.dto.LeadStatusRequestDTO;
import com.autohub.entity.Dealer;
import com.autohub.entity.Lead;
import com.autohub.entity.Vehicle;
import com.autohub.enums.DealerStatus;
import com.autohub.enums.LeadStatus;
import com.autohub.exception.ResourceNotFoundException;
import com.autohub.repository.DealerRepository;
import com.autohub.repository.LeadRepository;
import com.autohub.repository.VehicleRepository;
import com.autohub.service.LeadService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeadServiceImpl implements LeadService {

    private final LeadRepository leadRepository;

    private final VehicleRepository vehicleRepository;

    private final DealerRepository dealerRepository;

    private final ModelMapper modelMapper;

    @Override
    public LeadResponseDTO createLead(Long vehicleId, Long dealerId, LeadRequestDTO leadRequestDTO) {

        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(() ->
                new ResourceNotFoundException("Vehicle not found with id: " + vehicleId));

        Dealer dealer = dealerRepository.findById(dealerId)
                         .orElseThrow(() ->  new ResourceNotFoundException("Dealer not found"));

        Lead lead = new Lead();
        lead.setCustomerName(leadRequestDTO.getCustomerName());
        lead.setCustomerMobile(leadRequestDTO.getCustomerMobile());
        lead.setCustomerEmail(leadRequestDTO.getCustomerEmail());
        lead.setCustomerCity(leadRequestDTO.getCustomerCity());
        lead.setLeadStatus(LeadStatus.NEW);
        lead.setEnquiryDate(LocalDateTime.now());
        lead.setVehicle(vehicle);
        lead.setDealer(dealer);

        Lead saved = leadRepository.save(lead);


        return LeadResponseDTO.builder()
                .id(saved.getId())
                .customerName(saved.getCustomerName())
                .customerMobile(saved.getCustomerMobile())
                .customerEmail(saved.getCustomerEmail())
                .customerCity(saved.getCustomerCity())
                .vehicleName(saved.getVehicle().getBrand() + " "+ saved.getVehicle().getModel())
                .enquiryDate(saved.getEnquiryDate())
                .dealer(saved.getDealer().getId())
                .leadStatus(saved.getLeadStatus())
                .build();

    }

    @Override
    public List<LeadResponseDTO> getDealerLeads(Long dealerId) {

        List<Lead> leads = leadRepository.findByDealerId(dealerId);

        if (leads.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No leads found for dealer id : " + dealerId);
        }
        return leads.stream()
                .map(lead -> LeadResponseDTO.builder()
                        .id(lead.getId())
                        .customerName(lead.getCustomerName())
                        .customerMobile(lead.getCustomerMobile())
                        .customerEmail(lead.getCustomerEmail())
                        .customerCity(lead.getCustomerCity())
                        .vehicleName(lead.getVehicle().getBrand() + " "+ lead.getVehicle().getModel())
                        .dealer(lead.getDealer().getId())
                        .leadStatus(lead.getLeadStatus())
                        .enquiryDate(lead.getEnquiryDate())
                        .build())
                .toList();

    }

    @Override
    public LeadResponseDTO updateLeadStatus(Long leadId, LeadStatusRequestDTO requestDTO) {

        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Dealer not found"));

        if (requestDTO.getStatus() == null ||
                requestDTO.getStatus().trim().isEmpty()) {

            throw new RuntimeException("Status is required");
        }

        LeadStatus newStatus;

        try {
            newStatus = LeadStatus.valueOf(
                    requestDTO.getStatus().trim().toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException(
                    "Invalid status. Only PENDING or CONTACTED or CONVERTED are allowed");
        }

        LeadStatus currentStatus = lead.getLeadStatus();

        if (currentStatus == null) {
            currentStatus = LeadStatus.NEW;
        }

        if (currentStatus.equals(newStatus)) {
            throw new RuntimeException(
                    "Lead status already " + currentStatus);
        }

        lead.setLeadStatus(newStatus);

        Lead saved = leadRepository.save(lead);

        return LeadResponseDTO.builder()
                .id(saved.getId())
                .customerName(saved.getCustomerName())
                .customerMobile(saved.getCustomerMobile())
                .customerEmail(saved.getCustomerEmail())
                .customerCity(saved.getCustomerCity())
                .vehicleName(saved.getVehicle().getBrand() + " "+ saved.getVehicle().getModel())
                .enquiryDate(saved.getEnquiryDate())
                .dealer(saved.getDealer().getId())
                .leadStatus(saved.getLeadStatus())
                .build();
    }
}
