package com.airdnb.chat.dto;

import com.airdnb.chat.entity.ChatRoom;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ChatRoomRecipient {

    Long id;
    String recipientName;

    public static ChatRoomRecipient from(ChatRoom chatRoom, String recipientName) {
        return ChatRoomRecipient.builder()
            .id(chatRoom.getId())
            .recipientName(recipientName)
            .build();
    }


}
