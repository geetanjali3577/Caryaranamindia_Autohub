package com.autohub.serviceImpl;

import com.autohub.dto.VehicleRequestDTO;
import com.autohub.dto.VehicleResponseDTO;
import com.autohub.dto.VehicleStatusRequestDTO;
import com.autohub.entity.Vehicle;
import com.autohub.enums.VehicleStatus;
import com.autohub.exception.ResourceNotFoundException;
import com.autohub.repository.VehicleRepository;
import com.autohub.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.nio.file.*;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

   private final VehicleRepository vehicleRepository;

   private final ModelMapper modelMapper;

   @Override
   public VehicleResponseDTO addVehicle(VehicleRequestDTO request) {

       String vehicleId =String.format("VH%03d", vehicleRepository.count() + 1);

       Vehicle vehicle = Vehicle.builder()
                    .vehicleId(vehicleId)
                    .dealerId(request.getDealerId())
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
                    .featured(request.getFeatured())
                    .dealerContactName(request.getDealerContactName())
                    .dealerContactNumber(request.getDealerContactNumber())
                    .dealerContactEmail(request.getDealerContactEmail())
                    .createdAt(LocalDateTime.now())
                    .status(VehicleStatus.ACTIVE)
                    .build();

       Vehicle savedVehicle = vehicleRepository.save(vehicle);

       return VehicleResponseDTO.builder()
                    .vehicleId(savedVehicle.getVehicleId())
                    .dealerId(savedVehicle.getDealerId())
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
                    .featured(savedVehicle.getFeatured())
                    .dealerContactName(savedVehicle.getDealerContactName())
                    .dealerContactNumber(savedVehicle.getDealerContactNumber())
                    .dealerContactEmail(savedVehicle.getDealerContactEmail())
                    .status(String.valueOf(VehicleStatus.ACTIVE))
                    .createdAt(savedVehicle.getCreatedAt())
                    .build();

        }

    @Override
    public VehicleResponseDTO updateVehicle(String vehicleId, VehicleRequestDTO request) {

        Vehicle vehicle = vehicleRepository.findByVehicleId(vehicleId).orElseThrow(() ->
                        new ResourceNotFoundException("Vehicle not found with id: " + vehicleId));

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
        vehicle.setFeatured(request.getFeatured());
        vehicle.setDealerContactName(request.getDealerContactName());
        vehicle.setDealerContactNumber(request.getDealerContactNumber());
        vehicle.setDealerContactEmail(request.getDealerContactEmail());
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);

        return modelMapper.map(updatedVehicle, VehicleResponseDTO.class);
    }


    @Override
    public VehicleResponseDTO updateVehicleStatus(String vehicleId,VehicleStatusRequestDTO request) {

        Vehicle vehicle = vehicleRepository.findByVehicleId(vehicleId).orElseThrow(() ->
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
                .status(String.valueOf(updatedVehicle.getStatus()))
                .build();
    }
}
