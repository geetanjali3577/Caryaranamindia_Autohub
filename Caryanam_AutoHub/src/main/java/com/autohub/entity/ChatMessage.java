package com.autohub.entity;

import com.autohub.enums.MessageStatus;
import com.autohub.enums.UserType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_message")
@Data
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long conversationId;

    private Long senderId;

    @Enumerated(EnumType.STRING)
    private UserType senderType;

    private Long receiverId;

    @Enumerated(EnumType.STRING)
    private UserType receiverType;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    private LocalDateTime sentAt;

    private LocalDateTime deliveredAt;

    private LocalDateTime seenAt;
}