package com.autohub.dto;

import com.autohub.enums.UserType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ConversationDTO {

    private Long conversationId;

    private Long otherUserId;

    private UserType otherUserType;

    private String lastMessage;

    private LocalDateTime lastMessageTime;

    private Integer unreadCount;
}
