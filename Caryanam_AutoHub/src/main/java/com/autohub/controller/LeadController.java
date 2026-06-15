package com.autohub.controller;

import com.autohub.dto.*;
import com.autohub.service.LeadService;
import com.autohub.service.VehicleViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lead")
@RequiredArgsConstructor
public class LeadController {

    private final LeadService leadService;

    private final VehicleViewService vehicleViewService;

    // =============== GENERATE LEAD ON VEHICLE FROM CUSTOMER =====================

    @PostMapping("/generate-lead/{vehicleId}")
    public ResponseEntity<ResponseDto<LeadResponseDTO>> createNewLead(
            @PathVariable Long vehicleId,
            @RequestBody LeadRequestDTO requestDTO) {

        LeadResponseDTO lead = leadService.createLead(vehicleId, requestDTO);


        return ResponseEntity.ok(new ResponseDto<>(200,"New lead created successfully",lead));
    }

    // =============== GENERATE VIEW ON VEHICLE FROM CUSTOMER =====================

    @GetMapping("/generate-view/{vehicleId}")
    public ResponseEntity<ResponseDto<VehicleResponseDTO>> getVehicleById(@PathVariable Long vehicleId) {

        vehicleViewService.saveView(vehicleId);

        VehicleResponseDTO vehicleById = vehicleViewService.getVehicleById(vehicleId);

        return ResponseEntity.ok(new ResponseDto<>(200,"New View Added successfully",vehicleById));
    }

    // ================= ALL LEADS OF CUSTOMER =================

    @GetMapping("/{dealerId}")
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

}
