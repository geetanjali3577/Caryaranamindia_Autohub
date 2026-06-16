package com.autohub.controller;

import com.autohub.dto.DealerAccountStatusRequestDTO;
import com.autohub.dto.DealerResponseDTO;
import com.autohub.dto.DealerSubscriptionResponseDTO;
import com.autohub.dto.ResponseDto;
import com.autohub.service.DealerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final DealerService dealerService;

    // ================= CHANGE DEALER ACCOUNT STATUS :  APPROVED, PENDING =================
    @PutMapping("/dealer-status/{dealerId}")
    @Operation(summary = "Change Dealer Account Status By Admin API : Approve or Pending  ")
    public ResponseEntity<ResponseDto<DealerResponseDTO>> updateDealerStatus(
            @PathVariable Long dealerId,
            @RequestBody DealerAccountStatusRequestDTO requestDTO) {
        DealerResponseDTO response = dealerService.updateDealerAccountStatus(dealerId, requestDTO);

        return ResponseEntity.ok(new ResponseDto<>(200,"Dealer account approved successfully",response));
    }

    // ================= GET ALL HISTORY OF SUBSCRIPTION PLAN PURCHASED BY DEALER =================

    @GetMapping("/subscriptions")
    @Operation(summary = "See All Subscription Plan Purchased By Dealer API for Admin ")
    public ResponseEntity<ResponseDto<List<DealerSubscriptionResponseDTO>>> getSubscriptions() {

        List<DealerSubscriptionResponseDTO> data = dealerService.getSubscriptions();
        return ResponseEntity.ok(new ResponseDto<>(200, "All Purchase Subscriptions  fetched of Dealer Fetch Successfully", data));
    }
}
