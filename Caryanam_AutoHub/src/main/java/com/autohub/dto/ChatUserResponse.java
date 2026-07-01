package com.autohub.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatUserResponse {

    private Long id;

    private String name;

    private String role;

    private String chatKey;
}

