package com.autohub.entity;

import com.autohub.enums.DealerStatus;
import com.autohub.enums.Role;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "Dealer_Reg")
@Data
public class Dealer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String dealerId;  //Need to autogenerate

    @Column(nullable = false)
    private String businessName;

    @Column(nullable = false)
    private String ownerName;

    @Column(nullable = false)
    private String gstNumber;

    @Column(nullable = false)
    private Integer yearsInBusiness;

    @Column(nullable = false)
    private String mobile;

    @Column(nullable = false)
    private String whatsapp;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String pinCode;

    private String dealerLogo;
    private String showroomImage;

    @Enumerated(EnumType.STRING)
    private Role role;


    private String otp;

    private LocalDateTime otpGeneratedTime;

    private Boolean isOtpVerified = false;

    @Enumerated(EnumType.STRING)
    private DealerStatus status; // PENDING, APPROVED, REJECTED

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}