package com.autohub.service;

import com.autohub.dto.VehicleRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface VehicleService {

    String addVehicle(VehicleRequestDTO dto,List<MultipartFile> images,MultipartFile video) throws IOException;
}