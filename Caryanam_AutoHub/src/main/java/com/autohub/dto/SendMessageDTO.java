package com.autohub.dto;

import com.autohub.enums.UserType;
import lombok.Data;

@Data
public class SendMessageDTO {

    private Long senderId;

    private UserType senderType;

    private Long receiverId;

    private UserType receiverType;

    private String message;
}
