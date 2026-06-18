package com.autohub.entity;

import com.autohub.enums.CustomerLeadStatus;
import com.autohub.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Customer-Leads")
@Data
public class CustomerLead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;

    private String customerMobile;

    @Column(unique = true)
    private String customerEmail;

    private String customerPassword;

    private String customerCity;

    @CreationTimestamp
    private LocalDateTime enquiryDate;

    @CreationTimestamp
    private LocalDateTime accountCreatedAt;

    @Enumerated(EnumType.STRING)
    private CustomerLeadStatus leadStatus;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    private Vehicle vehicle;

    @ManyToOne
    private Dealer dealer;
}
