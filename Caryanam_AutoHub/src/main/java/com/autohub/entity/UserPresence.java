package com.autohub.entity;

import com.autohub.enums.UserType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_presence")
@Data
public class UserPresence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private Boolean online;

    private LocalDateTime lastSeen;
}
