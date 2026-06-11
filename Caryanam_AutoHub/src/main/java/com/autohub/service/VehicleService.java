package com.autohub.service;

import com.autohub.dto.VehicleRequestDTO;
import com.autohub.dto.VehicleResponseDTO;
import com.autohub.dto.VehicleStatusRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface VehicleService {

    VehicleResponseDTO addVehicle(VehicleRequestDTO vehicleRequestDTO);

    VehicleResponseDTO updateVehicle(String vehicleId,VehicleRequestDTO request);

    VehicleResponseDTO updateVehicleStatus(String vehicleId,VehicleStatusRequestDTO request);
}