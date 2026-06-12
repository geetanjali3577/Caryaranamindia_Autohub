package com.autohub.controller;

import com.autohub.dto.ResponseDto;
import com.autohub.dto.VehicleRequestDTO;
import com.autohub.dto.VehicleResponseDTO;
import com.autohub.dto.VehicleStatusRequestDTO;
import com.autohub.enums.VehicleStatus;
import com.autohub.service.VehicleMediaService;
import com.autohub.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping("/api/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    @Qualifier("vehicleMediaService")
    private final VehicleMediaService mediaService;
    private final VehicleService vehicleService;

    // ================= ADD VEHICLE INFO=================

    @PostMapping("/add/{dealerId}")
    public ResponseEntity<ResponseDto> addVehicle(
            @Valid @RequestBody VehicleRequestDTO vehicleRequestDTO,
            @PathVariable("dealerId") Long dealerId) {

        VehicleResponseDTO vehicleResponseDTO =
                vehicleService.addVehicle(vehicleRequestDTO, dealerId);

        return new ResponseEntity<>(
                new ResponseDto<>(201,
                        "Vehicle Added Successfully",
                        vehicleResponseDTO),
                HttpStatus.CREATED);
    }

    // ================= ADD VEHICLE IMAGE & VIDEO =================

    @PostMapping(value = "/upload-media/{vehicleId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<String>> uploadMedia( @PathVariable Long id,
                                                            @RequestPart("images") List<MultipartFile> images,
                                                            @RequestPart("video") MultipartFile video) throws IOException {

        String response =mediaService.uploadVehicleMedia(id,images,video);

        return ResponseEntity.ok(
                new ResponseDto<>(200,"Image Upload Successfully !!",response));
    }

    // ================= UPDATE VEHICLE INFO=================

    @PutMapping("/update/{vehicleId}")
    public ResponseEntity<ResponseDto<VehicleResponseDTO>> updateVehicle(@PathVariable Long id,
                                                                         @RequestBody VehicleRequestDTO request) {

        VehicleResponseDTO response = vehicleService.updateVehicle(id, request);

        return ResponseEntity.ok(new ResponseDto<>(200,"Vehicle Updated Successfully",response));

    }

    // ================= UPDATE VEHICLE STATUS =================

    @PatchMapping("/status/{vehicleId}")
    public ResponseEntity<ResponseDto<VehicleStatus>> updateVehicleStatus(
            @PathVariable Long id,
            @RequestBody VehicleStatusRequestDTO request) {

        VehicleResponseDTO response = vehicleService.updateVehicleStatus(id,request);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        200,
                        "Vehicle Status Updated Successfully",
                        response.getVehicleStatus()
                ));
    }

}
