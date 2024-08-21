package com.airdnb.chat;

import com.airdnb.chat.dto.MessageCreationRequest;
import com.airdnb.chat.dto.MessageResponse;
import com.airdnb.chat.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate template;
    private final ChatService chatService;


    @EventListener
    public void connectListener(SessionConnectEvent event) {
        log.info("Connected: {}", event);
    }

    @EventListener
    public void disconnectListener(SessionDisconnectEvent event) {
        log.info("Disconnected: {}", event);
    }

    @MessageMapping("/chat")
    public void sendMessage(@Valid @Payload MessageCreationRequest messageRequest) {
        MessageResponse messageResponse = chatService.createChat(messageRequest.toMessageCreation());
        template.convertAndSend("/sub/chat/room/" + messageRequest.getRoomId(), messageResponse);
    }
}
