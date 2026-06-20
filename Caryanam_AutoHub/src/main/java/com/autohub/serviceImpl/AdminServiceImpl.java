package com.autohub.serviceImpl;

import com.autohub.dto.*;
import com.autohub.entity.CustomerLead;
import com.autohub.entity.Dealer;
import com.autohub.entity.Vehicle;
import com.autohub.enums.DealerStatus;
import com.autohub.exception.ResourceNotFoundException;
import com.autohub.repository.AdminRepository;
import com.autohub.repository.CustomerLeadRepository;
import com.autohub.repository.DealerRepository;
import com.autohub.repository.VehicleRepository;
import com.autohub.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final DealerRepository dealerRepository;

    private final CustomerLeadRepository customerLeadRepository;

    private final VehicleRepository vehicleRepository;

    @Value("${server.port}")
    private String port;


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


    //All leads
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
                        .vehicleName(lead.getVehicle().getBrand()+" "+lead.getVehicle().getBrand()+" "+lead.getVehicle().getRegistrationYear())
                        .build())
                .toList();
    }

    //All pending dealer
    @Override
    public PendingDealerCountResponseDTO getPendingDealerCount() {

        long count = dealerRepository.countByDealerAccountStatus(DealerStatus.PENDING);

        return PendingDealerCountResponseDTO.builder()
                .totalPendingDealers(count)
                .build();
    }

    @Override
    public List<VehicleResponseDTO> getAllVehicle() {
        List<Vehicle> vehicles = vehicleRepository.findAll();

        if (vehicles.isEmpty()) {
            throw new ResourceNotFoundException("No Vehicles");
        }

        return vehicles.stream()
                .map(vehicle -> VehicleResponseDTO.builder()
                        .id(vehicle.getId())
                        .dealerId(vehicle.getDealer().getId())
                        .brand(vehicle.getBrand())
                        .model(vehicle.getModel())
                        .variant(vehicle.getVariant())
                        .registrationYear(vehicle.getRegistrationYear())
                        .askingPrice(vehicle.getAskingPrice())
                        .kilometerDriven(vehicle.getKilometerDriven())
                        .fuelType(vehicle.getFuelType())
                        .transmission(vehicle.getTransmission())
                        .ownershipDetails(vehicle.getOwnershipDetails())
                        .insuranceStatus(vehicle.getInsuranceStatus())
                        .vehicleDescription(vehicle.getVehicleDescription())
                        .city(vehicle.getCity())
                        .dealerContactName(vehicle.getDealer().getOwnerName())
                        .dealerContactNumber(vehicle.getDealer().getMobile())
                        .dealerWhatsappNumber(vehicle.getDealer().getWhatsapp())
                        .dealerBusinessName(vehicle.getDealer().getBusinessName())
                        .dealerContactEmail(vehicle.getDealer().getEmail())
                        .vehicleStatus(vehicle.getVehicleStatus())
                        .createdAt(vehicle.getCreatedAt())
                        .images(
                                vehicle.getMediaList() == null
                                        ? List.of()
                                        : vehicle.getMediaList().stream()
                                        .filter(media -> "IMAGE".equalsIgnoreCase(media.getMediaType()))
                                        //.map(VehicleMedia::getFilePath)
                                        .map(media -> "http://localhost:"+port+"/" +
                                                media.getFilePath().replace("\\", "/"))
                                        .toList()
                        )

                        .videos(
                                vehicle.getMediaList() == null
                                        ? List.of()
                                        : vehicle.getMediaList().stream()
                                        .filter(media -> "VIDEO".equalsIgnoreCase(media.getMediaType()))
                                        //.map(VehicleMedia::getFilePath)
                                        .map(media ->"http://localhost:"+port+ "/" +
                                                media.getFilePath().replace("\\", "/"))
                                        .toList()
                        )

                        .build())
                .toList();
    }

    @Override
    public VehicleCountResponseDTO getTotalVehicleCount() {

        return VehicleCountResponseDTO.builder()
                .totalVehicles(vehicleRepository.count())
                .build();
    }

    @Override
    public CustomerLeadCountResponseDTO getTotalCustomerLeadCount() {

        return CustomerLeadCountResponseDTO.builder()
                .totalCustomerLeads(customerLeadRepository.count())
                .build();
    }

}

