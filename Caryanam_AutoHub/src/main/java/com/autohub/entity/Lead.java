package com.autohub.entity;

import com.autohub.enums.LeadStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Customer-Leads")
@Data
public class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;

    private String customerMobile;

    private String customerEmail;

    private String customerCity;

    @CreationTimestamp
    private LocalDateTime enquiryDate;

    @Enumerated(EnumType.STRING)
    private LeadStatus leadStatus;

    @ManyToOne
    private Vehicle vehicle;

    @ManyToOne
    private Dealer dealer;
}
