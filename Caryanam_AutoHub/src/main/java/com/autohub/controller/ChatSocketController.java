package com.autohub.controller;

import com.autohub.dto.SendMessageDTO;
import com.autohub.dto.TypingDTO;
import com.autohub.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final ChatService chatService;

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/send-message")
    public void sendMessage(
            SendMessageDTO dto
    ) {

        chatService.sendMessage(dto);

//        messagingTemplate.convertAndSendToUser(
//                        dto.getReceiverId().toString(),
//                        "/queue/messages",
//                        dto
//                );

        messagingTemplate.convertAndSend(
                "/topic/messages/" + dto.getReceiverId(),dto);
    }

    @MessageMapping("/typing")
    public void typing(
            TypingDTO dto
    ) {

        messagingTemplate
                .convertAndSendToUser(
                        dto.getReceiverId().toString(),
                        "/queue/typing",
                        dto
                );
    }
}
