package com.autohub.service;

import com.autohub.dto.VehicleRequestDTO;
import com.autohub.dto.VehicleResponseDTO;
import com.autohub.dto.VehicleStatusRequestDTO;
import com.autohub.entity.Vehicle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface VehicleService {

    VehicleResponseDTO addVehicle(VehicleRequestDTO vehicleRequestDTO, Long id);

    VehicleResponseDTO updateVehicle(Long id, VehicleRequestDTO request);

    VehicleResponseDTO updateVehicleStatus(Long id, VehicleStatusRequestDTO request);

    List<Vehicle> getAllActiveVehicles();
}


