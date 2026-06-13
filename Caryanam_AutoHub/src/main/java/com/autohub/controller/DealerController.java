package com.autohub.controller;

import com.autohub.dto.*;
import com.autohub.service.DealerService;


import com.autohub.service.LeadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@RestController
@RequestMapping("/api/dealer")
@RequiredArgsConstructor
public class DealerController {

    private final DealerService dealerService;
    private final ObjectMapper objectMapper;
    private final LeadService leadService;


    // ================= REGISTER DEALER =================

    @PostMapping(value = "/register",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<DealerResponseDTO>> registerDealer(@RequestPart("dealer") String dealerJson,
                                            @RequestPart("dealerLogo")MultipartFile dealerLogo,
                                            @RequestPart("showroomImage") MultipartFile showroomImage) throws Exception {

        DealerRegisterDTO dto =objectMapper.readValue(dealerJson, DealerRegisterDTO.class);

        DealerResponseDTO dealerResponseDTO = dealerService.registerDealer(dto, dealerLogo, showroomImage);

        return new ResponseEntity<>(new ResponseDto(200,"Dealer Registration Successfully",dealerResponseDTO),HttpStatus.OK);
    }

    // ================= UPDATE DEALER PROFILE =================

    @PutMapping("/profile/{dealerId}")
    public ResponseEntity<ResponseDto<DealerProfileResponseDTO>> updateDealerProfile(@PathVariable Long dealerId,@Valid @RequestBody UpdateDealerProfileRequestDTO request) {

        DealerProfileResponseDTO dealerResponseDTO = dealerService.updateDealerProfile(dealerId, request);

        return new ResponseEntity<>(new ResponseDto<>(200,"Dealer Profile Updated Successfully",dealerResponseDTO),HttpStatus.OK);
    }

    // ================= ALL LEADS =================

    @GetMapping("/leads/{dealerId}")
    public ResponseEntity<ResponseDto<List<LeadResponseDTO>>> getCustomerLeads(
            @PathVariable Long dealerId) {

        List<LeadResponseDTO> response = leadService.getDealerLeads(dealerId);

        return ResponseEntity.ok(new ResponseDto<>(200,"Customer All Leads Fetched Successfully",response)
        );
    }

    // ================= UPDATE LEADS STATUS=================

    @PutMapping("/lead-status/{leadId}")
    public ResponseEntity<ResponseDto<LeadResponseDTO>> updateLeadStatus(
            @PathVariable Long leadId,
            @RequestBody LeadStatusRequestDTO requestDTO) {
        LeadResponseDTO response = leadService.updateLeadStatus(leadId, requestDTO);

        return ResponseEntity.ok(new ResponseDto<>(200,"Lead status successfully",response));
    }


   // ================= DEALER DASHBOARD =================

    @GetMapping("/dashboard/{dealerId}")
    public ResponseEntity<DashboardResponseDTO>   getDashboard(@PathVariable Long dealerId) {

        return ResponseEntity.ok( dealerService.getDashboard(dealerId));
    }

    // ================= SUBSCRIPTION =================

    @GetMapping("/subscriptions")
    public ResponseEntity<ResponseDto<List<DealerSubscriptionResponseDTO>>> getSubscriptions() {

        List<DealerSubscriptionResponseDTO> data = dealerService.getSubscriptions();
        return ResponseEntity.ok(new ResponseDto<>(200, "Subscriptions fetched successfully", data));
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