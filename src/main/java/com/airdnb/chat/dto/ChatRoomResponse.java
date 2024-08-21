package com.airdnb.chat.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ChatRoomResponse {

    Long id;
    String recipientName;

    public static ChatRoomResponse from(ChatRoomRecipient chatRoomRecipient) {
        return ChatRoomResponse.builder()
            .id(chatRoomRecipient.getId())
            .recipientName(chatRoomRecipient.getRecipientName())
            .build();
    }


}
