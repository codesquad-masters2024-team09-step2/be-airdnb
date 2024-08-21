package com.airdnb.chat.dto;

import com.airdnb.chat.service.ChatRoomCreation;
import com.airdnb.global.constants.ChatConstants;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class ChatRoomCreationRequest {

    @NotNull
    String senderId;
    @NotNull
    String receiverId;
    @NotNull
    String role;

    public ChatRoomCreation toChatRoomCreation() {
        if (role.equals(ChatConstants.HOST)) {
            return fromHost();
        }

        return fromGuest();

    }

    private ChatRoomCreation fromHost() {
        return ChatRoomCreation.builder()
            .hostId(senderId)
            .guestId(receiverId)
            .build();
    }

    private ChatRoomCreation fromGuest() {
        return ChatRoomCreation.builder()
            .hostId(receiverId)
            .guestId(senderId)
            .build();
    }
}
