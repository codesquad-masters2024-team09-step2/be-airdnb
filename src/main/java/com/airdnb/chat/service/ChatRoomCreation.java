package com.airdnb.chat.service;

import com.airdnb.chat.dto.ChatRoomCreationRequest;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ChatRoomCreation {

    String hostId;
    String guestId;

    public static ChatRoomCreation from(ChatRoomCreationRequest chatRoomCreationRequest) {
        if (chatRoomCreationRequest.getRole().equals("host")) {
            return ChatRoomCreation.builder()
                .hostId(chatRoomCreationRequest.getSenderId())
                .guestId(chatRoomCreationRequest.getReceiverId())
                .build();
        }
        return ChatRoomCreation.builder()
            .hostId(chatRoomCreationRequest.getReceiverId())
            .guestId(chatRoomCreationRequest.getSenderId())
            .build();
    }
}
