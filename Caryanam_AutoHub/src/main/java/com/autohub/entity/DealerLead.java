package com.autohub.entity;

import com.autohub.enums.LeadStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "dealer_leads")
@Data
public class DealerLead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String leadId; // LD001

    private String customerName;

    private String mobile;

    private String email;

    @ManyToOne
    @JoinColumn(name = "dealer_id")
    private Dealer dealer;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Enumerated(EnumType.STRING)
    private LeadStatus status = LeadStatus.NEW;

    private LocalDateTime enquiryDate;

    @PrePersist
    public void prePersist() {
        enquiryDate = LocalDateTime.now();
    }
}
