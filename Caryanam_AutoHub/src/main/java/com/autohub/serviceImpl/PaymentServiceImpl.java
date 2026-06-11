package com.autohub.serviceImpl;



import com.autohub.dto.PaymentRequestDTO;
import com.autohub.dto.ResponseDto;
import com.autohub.entity.Dealer;
import com.autohub.entity.Payment;
import com.autohub.repository.DealerRepository;
import com.autohub.repository.PaymentRepository;
import com.autohub.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final DealerRepository dealerRepository;

    @Override
    public ResponseDto<?> createPayment(
            PaymentRequestDTO dto) {

        Payment payment = new Payment();

        payment.setDealerId(String.valueOf(dto.getDealerId()));

        payment.setSubscriptionPlan(
                dto.getSubscriptionPlan());

        payment.setAmount(
                dto.getSubscriptionPlan()
                        .getAmount());

        payment.setPaymentStatus("PENDING");

        paymentRepository.save(payment);

        return new ResponseDto<>(
                200,
                "Payment Created Successfully",
                payment
        );
    }

    @Override
    public ResponseDto paymentSuccess(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found"));

        payment.setPaymentStatus("SUCCESS");

        payment.setPaymentDate(
                LocalDateTime.now());

        payment.setTransactionId(
                UUID.randomUUID().toString());

        paymentRepository.save(payment);

        Dealer dealer = dealerRepository.findById(
                        Long.valueOf(payment.getDealerId()))
                .orElseThrow(() ->
                        new RuntimeException("Dealer not found"));

        dealer.setSubscriptionActive(true);

        dealer.setSubscriptionPlan(
                payment.getSubscriptionPlan());

        dealer.setSubscriptionStartDate(
                LocalDateTime.now());

        dealer.setSubscriptionEndDate(
                LocalDateTime.now().plusMonths(1));

        dealerRepository.save(dealer);

        return new ResponseDto<>(200, "Subscription Activated Successfully", null);
    }

    @Override
    public ResponseDto paymentFailed(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found"));

        payment.setPaymentStatus("FAILED");

        paymentRepository.save(payment);

        return new ResponseDto<>(
                400,
                "Payment Failed",
                null
        );
    }
    @Override
    public ResponseDto<?> getAllPayments() {

        List<Payment> payments =
                paymentRepository.findAll();

        return new ResponseDto<>(
                200,
                "All Payments Fetched Successfully",
                payments
        );
    }
    @Override
    public ResponseDto<?> getDealerPayments(
            Long Id) {

        List<Payment> payments =
                paymentRepository
                        .findByDealerId(String.valueOf(Id));

        return new ResponseDto<>(
                200,
                "Dealer Payment History Fetched Successfully",
                payments
        );
    }
}