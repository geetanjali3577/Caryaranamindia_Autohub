package com.autohub.entity;

import com.autohub.enums.CustomerLeadStatus;
import com.autohub.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Customer-Registration")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String mobile;

    @Column(nullable = false)
    private String customerCity;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    private LocalDateTime accountCreatedAt;

    @Enumerated(EnumType.STRING)
    private Role role;

}
