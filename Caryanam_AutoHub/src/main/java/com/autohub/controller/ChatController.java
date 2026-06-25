package com.autohub.controller;

import com.autohub.entity.ChatMessage;
import com.autohub.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/messages/{conversationId}")
    public List<ChatMessage> getMessages(
            @PathVariable Long conversationId
    ) {
        return chatService.getMessages(
                conversationId
        );
    }

    @PutMapping("/seen/{messageId}")
    public String seen(
            @PathVariable Long messageId
    ) {

        chatService.markSeen(
                messageId
        );

        return "Seen Updated";
    }

    @GetMapping("/unread/{userId}")
    public Long unread(
            @PathVariable Long userId
    ) {
        return chatService.unreadCount(
                userId
        );
    }
}