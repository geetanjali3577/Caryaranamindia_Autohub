package com.autohub.serviceImpl;

import com.autohub.dto.SendMessageDTO;
import com.autohub.entity.ChatConversation;
import com.autohub.entity.ChatMessage;
import com.autohub.enums.MessageStatus;
import com.autohub.repository.ChatConversationRepository;
import com.autohub.repository.ChatMessageRepository;
import com.autohub.service.ChatService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatServiceImpl
        implements ChatService {

    private final ChatConversationRepository conversationRepository;
    private final ChatMessageRepository messageRepository;

    @Override
    public void sendMessage(
            SendMessageDTO dto
    ) {

        ChatConversation conversation =
                conversationRepository
                        .findConversation(
                                dto.getSenderId(),
                                dto.getReceiverId()
                        )
                        .orElseGet(() -> {

                            ChatConversation c =
                                    new ChatConversation();

                            c.setUser1Id(
                                    dto.getSenderId()
                            );

                            c.setUser1Type(
                                    dto.getSenderType()
                            );

                            c.setUser2Id(
                                    dto.getReceiverId()
                            );

                            c.setUser2Type(
                                    dto.getReceiverType()
                            );

                            return conversationRepository
                                    .save(c);
                        });

        ChatMessage message =
                new ChatMessage();

        message.setConversationId(
                conversation.getId()
        );

        message.setSenderId(
                dto.getSenderId()
        );

        message.setSenderType(
                dto.getSenderType()
        );

        message.setReceiverId(
                dto.getReceiverId()
        );

        message.setReceiverType(
                dto.getReceiverType()
        );

        message.setMessage(
                dto.getMessage()
        );

        message.setStatus(
                MessageStatus.SENT
        );

        message.setSentAt(
                LocalDateTime.now()
        );

        messageRepository.save(message);

        conversation.setLastMessage(
                dto.getMessage()
        );

        conversation.setLastMessageTime(
                LocalDateTime.now()
        );

        conversationRepository.save(
                conversation
        );
    }

    @Override
    public List<ChatMessage> getMessages(
            Long conversationId
    ) {
        return messageRepository
                .findByConversationIdOrderBySentAtAsc(
                        conversationId
                );
    }

    @Override
    public void markSeen(
            Long messageId
    ) {

        ChatMessage message =
                messageRepository
                        .findById(messageId)
                        .orElseThrow();

        message.setStatus(
                MessageStatus.SEEN
        );

        message.setSeenAt(
                LocalDateTime.now()
        );

        messageRepository.save(
                message
        );
    }

    @Override
    public Long unreadCount(
            Long userId
    ) {
        return messageRepository
                .countByReceiverIdAndStatus(
                        userId,
                        MessageStatus.SENT
                );
    }
}
