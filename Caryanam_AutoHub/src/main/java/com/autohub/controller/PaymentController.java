package com.autohub.controller;

import com.autohub.dto.PaymentRequestDTO;
import com.autohub.dto.ResponseDto;
import com.autohub.entity.Dealer;
import com.autohub.entity.Payment;
import com.autohub.enums.PaymentStatus;
import com.autohub.repository.DealerRepository;
import com.autohub.repository.PaymentRepository;
import com.autohub.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

// ================== DO PAYMENT FOR SUBSCRIPTION PLAN ======================
    @PostMapping("/subscription/purchase")
    public ResponseEntity<ResponseDto<?>> createPayment(
            @RequestBody PaymentRequestDTO dto) {

        return ResponseEntity.ok(paymentService.createPayment(dto));
    }

// ============ APPROVED PAYMENT BY ADMIN ===========

    @PutMapping("/success/{paymentId}")
    public ResponseEntity<ResponseDto<?>> paymentSuccess(
            @PathVariable Long paymentId) {

        return ResponseEntity.ok(
                paymentService.paymentSuccess(paymentId));
    }

    @PutMapping("/failed/{paymentId}")
    public ResponseEntity<ResponseDto<?>> paymentFailed(
            @PathVariable Long paymentId) {

        return ResponseEntity.ok(
                paymentService.paymentFailed(paymentId));
    }
    @GetMapping("/admin/history")
    public ResponseEntity<ResponseDto<?>> getAllPayments() {

        return ResponseEntity.ok(
                paymentService.getAllPayments());
    }


    @GetMapping("/dealer/{dealerId}")
    public ResponseEntity<ResponseDto<?>> getDealerPayments(
            @PathVariable Long Id) {

        return ResponseEntity.ok(paymentService.getDealerPayments(Id));
    }
}
