package com.autohub.controller;

import com.autohub.dto.DealerAccountStatusRequestDTO;
import com.autohub.dto.DealerResponseDTO;
import com.autohub.dto.ResponseDto;
import com.autohub.enums.DealerStatus;
import com.autohub.service.DealerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final DealerService dealerService;

    //Approve dealer account status
    @PutMapping("/dealer-status/{dealerId}")
    public ResponseEntity<ResponseDto<DealerResponseDTO>> updateDealerStatus(
            @PathVariable Long dealerId,
            @RequestBody DealerAccountStatusRequestDTO requestDTO) {
        DealerResponseDTO response = dealerService.updateDealerAccountStatus(dealerId, requestDTO);

        return ResponseEntity.ok(new ResponseDto<>(200,"Dealer account approved successfully",response));
    }
}
