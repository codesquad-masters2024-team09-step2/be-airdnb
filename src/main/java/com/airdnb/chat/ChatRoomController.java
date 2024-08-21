package com.airdnb.chat;

import com.airdnb.chat.dto.ChatRoomCreationRequest;
import com.airdnb.chat.dto.ChatRoomResponse;
import com.airdnb.chat.dto.MessageResponse;
import com.airdnb.chat.service.ChatRoomService;
import com.airdnb.global.ApiResponse;
import com.airdnb.global.UriMaker;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping
    public ResponseEntity<ApiResponse> createChatRoom(@Valid @RequestBody ChatRoomCreationRequest chatRoomCreationRequest) {
        Long roomId = chatRoomService.createChatRoom(chatRoomCreationRequest.toChatRoomCreation());
        URI location = UriMaker.makeUri(roomId);
        return ResponseEntity.created(location).body(ApiResponse.success(null));

    }

    @GetMapping
    public ResponseEntity<ApiResponse> getChatRooms() {
        List<ChatRoomResponse> chatRooms = chatRoomService.getChatRooms().stream().map(ChatRoomResponse::from).toList();
        return ResponseEntity.ok(ApiResponse.success(chatRooms));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ApiResponse> getMessagesFromRoom(@PathVariable Long roomId) {
        List<MessageResponse> messages = chatRoomService.getMessagesFromRoom(roomId).stream().map(MessageResponse::from).toList();
        return ResponseEntity.ok(ApiResponse.success(messages));
    }


}
