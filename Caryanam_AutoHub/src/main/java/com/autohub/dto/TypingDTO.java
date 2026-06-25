package com.autohub.dto;

import lombok.Data;

@Data
public class TypingDTO {

    private Long senderId;

    private Long receiverId;

    private Boolean typing;
}
