package com.autohub.entity;

import com.autohub.enums.SubscriptionPlan;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private String dealerId;

    @Enumerated(EnumType.STRING)
    private SubscriptionPlan subscriptionPlan;

    private Double amount;

    private String transactionId;

    private String paymentStatus;

    private LocalDateTime paymentDate;
}