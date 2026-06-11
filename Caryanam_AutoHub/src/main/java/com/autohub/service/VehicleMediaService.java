package com.autohub.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface VehicleMediaService {

    String uploadVehicleMedia(Long id,List<MultipartFile> images,MultipartFile video) throws IOException;
}
