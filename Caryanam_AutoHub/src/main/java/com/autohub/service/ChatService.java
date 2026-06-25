package com.autohub.service;

import com.autohub.dto.SendMessageDTO;
import com.autohub.entity.ChatMessage;

import java.util.List;

public interface ChatService {

    void sendMessage(
            SendMessageDTO dto
    );

    List<ChatMessage>
    getMessages(
            Long conversationId
    );

    void markSeen(
            Long messageId
    );

    Long unreadCount(
            Long userId
    );
}
