package com.airdnb.chat.dto;

import com.airdnb.chat.entity.ChatMessage;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class MessageResponse {

    Long roomId;
    String senderName;
    String message;
    LocalDateTime createdAt;

    public static MessageResponse from(ChatMessage chatMessage) {
        return MessageResponse.builder()
            .roomId(chatMessage.getChatRoom().getId())
            .senderName(chatMessage.getUserName())
            .message(chatMessage.getContent())
            .createdAt(chatMessage.getCreatedAt())
            .build();
    }
}
