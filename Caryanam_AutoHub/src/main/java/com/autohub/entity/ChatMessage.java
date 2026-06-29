package com.autohub.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Data
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomId;

    private Long senderId;

    private String senderRole;

    private Long receiverId;

    private String receiverRole;

    private Boolean isRead = false;

    private LocalDateTime readAt;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime sentAt =
            LocalDateTime.now();
}


