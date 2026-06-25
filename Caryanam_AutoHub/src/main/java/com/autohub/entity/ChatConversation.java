package com.autohub.entity;

import com.autohub.enums.UserType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_conversation")
@Data
public class ChatConversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long user1Id;

    @Enumerated(EnumType.STRING)
    private UserType user1Type;

    private Long user2Id;

    @Enumerated(EnumType.STRING)
    private UserType user2Type;

    private String lastMessage;

    private LocalDateTime lastMessageTime;

    private Integer unreadUser1Count = 0;

    private Integer unreadUser2Count = 0;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
