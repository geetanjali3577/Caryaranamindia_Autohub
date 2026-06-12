package com.autohub.serviceImpl;

import com.autohub.dto.VehicleRequestDTO;
import com.autohub.dto.VehicleResponseDTO;
import com.autohub.dto.VehicleStatusRequestDTO;
import com.autohub.entity.Dealer;
import com.autohub.entity.Vehicle;
import com.autohub.enums.SubscriptionPlan;
import com.autohub.enums.VehicleStatus;
import com.autohub.exception.ResourceNotFoundException;
import com.autohub.repository.DealerRepository;
import com.autohub.repository.VehicleRepository;
import com.autohub.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

   private final VehicleRepository vehicleRepository;
   private final DealerRepository dealerRepository;
   private final ModelMapper modelMapper;

    @Override
    public VehicleResponseDTO addVehicle(VehicleRequestDTO request,Long id) {
        Dealer dealer = dealerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Dealer not found"));
        log.info("Dealer Details : {}", dealer.getId());
        // Subscription Active आहे का?
        if (dealer.getSubscriptionActive() == null ||
                !dealer.getSubscriptionActive()) {

            throw new RuntimeException(
                    "Please purchase subscription before adding vehicles");
        }

        // Subscription Expired आहे का?
        if (dealer.getSubscriptionEndDate() != null &&
                dealer.getSubscriptionEndDate().isBefore(LocalDateTime.now())) {

            throw new RuntimeException(
                    "Subscription expired. Please renew subscription");
        }

        // Vehicle Count Check
        Long vehicleCount = vehicleRepository.countByDealer_Id((request.getDealerId()));

        if (dealer.getSubscriptionPlan() != SubscriptionPlan.PREMIUM) {

            int limit = dealer.getSubscriptionPlan()
                    .getVehicleLimit();

            if (vehicleCount >= limit) {

                throw new RuntimeException(
                        "Vehicle limit exceeded for "
                                + dealer.getSubscriptionPlan());
            }
        }

        String vehicleId =String.format("VH%03d", vehicleRepository.count() + 1);

        Vehicle vehicle = Vehicle.builder()
                .vehicleId(vehicleId)
                .dealer(dealer)
                .brand(request.getBrand())
                .model(request.getModel())
                .variant(request.getVariant())
                .manufacturingYear(request.getManufacturingYear())
                .registrationYear(request.getRegistrationYear())
                .fuelType(request.getFuelType())
                .transmission(request.getTransmission())
                .kilometerDriven(request.getKilometerDriven())
                .ownershipDetails(request.getOwnershipDetails())
                .insuranceStatus(request.getInsuranceStatus())
                .rtoInformation(request.getRtoInformation())
                .askingPrice(request.getAskingPrice())
                .vehicleDescription(request.getVehicleDescription())
                .financeAvailability(request.getFinanceAvailability())
                .dealerContactName(request.getDealerContactName())
                .dealerContactNumber(request.getDealerContactNumber())
                .dealerContactEmail(request.getDealerContactEmail())
                .createdAt(LocalDateTime.now())
                .status(VehicleStatus.ACTIVE)
                .build();

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return VehicleResponseDTO.builder()
                .vehicleId(savedVehicle.getVehicleId())
                .dealerId(savedVehicle.getDealer().getId())
                .brand(savedVehicle.getBrand())
                .model(savedVehicle.getModel())
                .variant(savedVehicle.getVariant())
                .manufacturingYear(savedVehicle.getManufacturingYear())
                .registrationYear(savedVehicle.getRegistrationYear())
                .fuelType(savedVehicle.getFuelType())
                .transmission(savedVehicle.getTransmission())
                .kilometerDriven(savedVehicle.getKilometerDriven())
                .ownershipDetails(savedVehicle.getOwnershipDetails())
                .insuranceStatus(savedVehicle.getInsuranceStatus())
                .rtoInformation(savedVehicle.getRtoInformation())
                .askingPrice(savedVehicle.getAskingPrice())
                .vehicleDescription(savedVehicle.getVehicleDescription())
                .financeAvailability(savedVehicle.getFinanceAvailability())
                .dealerContactName(savedVehicle.getDealerContactName())
                .dealerContactNumber(savedVehicle.getDealerContactNumber())
                .dealerContactEmail(savedVehicle.getDealerContactEmail())
                .vehicleStatus(VehicleStatus.valueOf(String.valueOf(VehicleStatus.ACTIVE)))
                .createdAt(savedVehicle.getCreatedAt())
                .build();

    }


    @Override
    public VehicleResponseDTO updateVehicle(Long id, VehicleRequestDTO request) {

        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() ->
                        new ResourceNotFoundException("Vehicle not found with id: " + id));

        vehicle.setBrand(request.getBrand());
        vehicle.setModel(request.getModel());
        vehicle.setVariant(request.getVariant());
        vehicle.setManufacturingYear(request.getManufacturingYear());
        vehicle.setRegistrationYear(request.getRegistrationYear());
        vehicle.setFuelType(request.getFuelType());
        vehicle.setTransmission(request.getTransmission());
        vehicle.setKilometerDriven(request.getKilometerDriven());
        vehicle.setOwnershipDetails(request.getOwnershipDetails());
        vehicle.setInsuranceStatus(request.getInsuranceStatus());
        vehicle.setRtoInformation(request.getRtoInformation());
        vehicle.setAskingPrice(request.getAskingPrice());
        vehicle.setVehicleDescription(request.getVehicleDescription());
        vehicle.setFinanceAvailability(request.getFinanceAvailability());
        vehicle.setDealerContactName(request.getDealerContactName());
        vehicle.setDealerContactNumber(request.getDealerContactNumber());
        vehicle.setDealerContactEmail(request.getDealerContactEmail());
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);

        return modelMapper.map(updatedVehicle, VehicleResponseDTO.class);
    }


    @Override
    public VehicleResponseDTO updateVehicleStatus(Long id,VehicleStatusRequestDTO request) {

        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vehicle not found"));

        if (!request.getStatus().equalsIgnoreCase("ACTIVE")
                && !request.getStatus().equalsIgnoreCase("INACTIVE")) {

            throw new RuntimeException("Status must be ACTIVE or INACTIVE");
        }

        vehicle.setStatus(VehicleStatus.valueOf(request.getStatus().toUpperCase()));

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);

        return VehicleResponseDTO.builder()
                .vehicleId(updatedVehicle.getVehicleId())
                .vehicleStatus(VehicleStatus.valueOf(String.valueOf(updatedVehicle.getStatus())))
                .build();
    }

    @Override
    public List<Vehicle> getAllActiveVehicles() {
        return List.of();
    }
}
