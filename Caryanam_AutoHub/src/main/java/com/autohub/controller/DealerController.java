package com.autohub.controller;

import com.autohub.dto.*;
import com.autohub.service.DealerService;


import com.autohub.service.VehicleMediaService;
import com.autohub.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/dealer")
@RequiredArgsConstructor
public class DealerController {

    private final DealerService dealerService;
   private final VehicleMediaService mediaService;
    private final VehicleService vehicleService;


    // ================= REGISTER DEALER =================


    @PostMapping("/register")
    public ResponseEntity<ResponseDto<DealerResponseDTO>> registerDealer(@Valid @RequestBody DealerRegisterDTO dto) {

        DealerResponseDTO response = dealerService.registerDealer(dto);
        return ResponseEntity.status(201)
                .body(new ResponseDto<>(201, "Dealer Registered Successfully", response));
    }

  // ================= ADD VEHICLE INFO=================

    @PostMapping("/add-vehicle")
    public ResponseEntity<ResponseDto> addVehicle(@Valid @RequestBody VehicleRequestDTO vehicleRequestDTO){

        VehicleResponseDTO vehicleResponseDTO = vehicleService.addVehicle(vehicleRequestDTO);

        return new ResponseEntity<>(new ResponseDto<>(201,"Vehicle Added Successfully ",vehicleResponseDTO), HttpStatus.CREATED);
    }

    // ================= ADD VEHICLE IMAGE & VIDEO =================

    @PostMapping(value = "/upload-media/{vehicleId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<String>> uploadMedia( @PathVariable String vehicleId,
                                                            @RequestPart("images") List<MultipartFile> images,
                                                            @RequestPart("video") MultipartFile video) throws IOException {

        String response =mediaService.uploadVehicleMedia(vehicleId,images,video);

        return ResponseEntity.ok(
                new ResponseDto<>(200,"Image Upload Successfully !!",response));
    }

    // ================= UPDATE VEHICLE INFO=================

    @PutMapping("/vehicle/update/{vehicleId}")
    public ResponseEntity<ResponseDto<VehicleResponseDTO>> updateVehicle(@PathVariable String vehicleId,
                                                                         @RequestBody VehicleRequestDTO request) {

        VehicleResponseDTO response = vehicleService.updateVehicle(vehicleId, request);

        return ResponseEntity.ok(new ResponseDto<>(200,"Vehicle Updated Successfully",response));

    }

    // ================= UPDATE VEHICLE STATUS =================

    @PatchMapping("/vehicle/status/{vehicleId}")
    public ResponseEntity<ResponseDto<String>> updateVehicleStatus(
            @PathVariable String vehicleId,
            @RequestBody VehicleStatusRequestDTO request) {

        VehicleResponseDTO response =
                vehicleService.updateVehicleStatus(
                        vehicleId,
                        request);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        200,
                        "Vehicle Status Updated Successfully",
                        response.getStatus()
                ));
    }

    // ================= FORGOT PASSWORD DEALER =================
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String email) {
        return ResponseEntity.ok(dealerService.sendOtp(email));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody VerifyOtpDTO dto) {
        return ResponseEntity.ok(dealerService.verifyOtp(dto));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO dto) {
        return ResponseEntity.ok(dealerService.resetPassword(dto));
    }

}