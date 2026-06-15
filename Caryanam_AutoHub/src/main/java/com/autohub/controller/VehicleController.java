package com.autohub.controller;

import com.autohub.dto.ResponseDto;
import com.autohub.dto.VehicleRequestDTO;
import com.autohub.dto.VehicleResponseDTO;
import com.autohub.dto.VehicleStatusRequestDTO;
import com.autohub.enums.VehicleStatus;
import com.autohub.service.VehicleService;
import com.autohub.service.VehicleViewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping("/api/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;


    private final VehicleViewService vehicleViewService;

    // ================= ADD VEHICLE INFO=================

    @PostMapping(value = "/add/{dealerId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> addVehicle(
           @Valid @RequestPart("vehicle")
            String vehicleJson,
            @RequestPart(value = "images",required = false)
            List<MultipartFile> images,
            @RequestPart(value = "videos",required = false)
            List<MultipartFile> videos, @PathVariable Long dealerId)throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        VehicleRequestDTO vehicleRequestDTO =mapper.readValue(vehicleJson,VehicleRequestDTO.class);

        VehicleResponseDTO response =vehicleService.addVehicleWithData(vehicleRequestDTO,images,videos,dealerId);

        return new ResponseEntity<>( new ResponseDto<>(201,"Vehicle Added Successfully",response),HttpStatus.CREATED);
    }



    // ================= UPDATE VEHICLE INFO=================

    @PutMapping("/update/{vehicleId}")
    public ResponseEntity<ResponseDto<VehicleResponseDTO>> updateVehicle(@PathVariable("vehicleId") Long id,
                                                                         @RequestBody VehicleRequestDTO request) {

        VehicleResponseDTO response = vehicleService.updateVehicle(id, request);

        return ResponseEntity.ok(new ResponseDto<>(200,"Vehicle Updated Successfully",response));

    }

    // ================= UPDATE VEHICLE STATUS =================

    @PatchMapping("/status/{vehicleId}")
    public ResponseEntity<ResponseDto<VehicleStatus>> updateVehicleStatus(
            @PathVariable("vehicleId") Long id,
            @RequestBody VehicleStatusRequestDTO request) {

        VehicleResponseDTO response = vehicleService.updateVehicleStatus(id,request);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        200,
                        "Vehicle Status Updated Successfully",
                        response.getVehicleStatus()
                ));
    }


    // ================= DELETE VEHICLE =================

    @DeleteMapping("/delete/{vehicleId}")
    public ResponseEntity<ResponseDto> deleteVehicle(@PathVariable Long vehicleId){

        vehicleService.deleteVehicle(vehicleId);
        return new ResponseEntity<>(new ResponseDto<>(201,"Vehicle Delete Successfully",null),HttpStatus.OK);
    }
    @GetMapping("/dealer/{dealerId}")
    public ResponseEntity<ResponseDto<List<VehicleResponseDTO>>> getAllVehicleByDealerId(
            @PathVariable Long dealerId) {

        List<VehicleResponseDTO> response =
                vehicleService.getAllVehicleByDealerId(dealerId);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        200,
                        "Vehicles fetched successfully",
                        response));
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<ResponseDto<VehicleResponseDTO>> getVehicleById(
            @PathVariable Long vehicleId) {

        VehicleResponseDTO response =
                vehicleService.getVehicleById(vehicleId);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        200,
                        "Vehicle fetched successfully",
                        response));
    }

}
