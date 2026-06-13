package com.autohub.controller;

import com.autohub.dto.*;
import com.autohub.service.LeadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lead")
@RequiredArgsConstructor
public class LeadController {

    private final LeadService leadService;

    @PostMapping("/new-lead/{vehicleId}/{dealerId}")
    public ResponseEntity<ResponseDto<LeadResponseDTO>> createNewLead(
            @PathVariable Long vehicleId,
            @PathVariable Long dealerId,
            @RequestBody LeadRequestDTO requestDTO) {

        LeadResponseDTO lead = leadService.createLead(vehicleId, dealerId, requestDTO);


        return ResponseEntity.ok(new ResponseDto<>(200,"New lead created successfully",lead));
    }
}
