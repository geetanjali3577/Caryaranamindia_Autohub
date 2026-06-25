package com.autohub.repository;

import com.autohub.entity.ChatMessage;
import com.autohub.enums.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository
        extends JpaRepository<ChatMessage,Long> {

    List<ChatMessage>
    findByConversationIdOrderBySentAtAsc(
            Long conversationId
    );

    Long countByReceiverIdAndStatus(
            Long receiverId,
            MessageStatus status
    );
}
