package com.autohub.controller;

import com.autohub.dto.*;
import com.autohub.service.DealerService;


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
@RequestMapping("/api/dealer")
@RequiredArgsConstructor
public class DealerController {

    private final DealerService dealerService;

    // ================= REGISTER DEALER =================
    @PostMapping("/register")
    public ResponseEntity<ResponseDto<DealerResponseDTO>> registerDealer(@Valid @RequestBody DealerRegisterDTO dto) {

        DealerResponseDTO response = dealerService.registerDealer(dto);
        return ResponseEntity.status(201)
                .body(new ResponseDto<>(201, "Dealer Registered Successfully", response));
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

    @GetMapping("/subscriptions")
    public ResponseEntity<ResponseDto<List<DealerSubscriptionResponseDTO>>> getSubscriptions() {

        List<DealerSubscriptionResponseDTO> data = dealerService.getSubscriptions();
        return ResponseEntity.ok(new ResponseDto<>(200, "Subscriptions fetched successfully", data));
    }

}