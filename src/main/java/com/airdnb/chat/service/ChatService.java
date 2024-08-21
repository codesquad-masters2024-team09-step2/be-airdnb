package com.airdnb.chat.service;

import com.airdnb.chat.dto.MessageResponse;
import com.airdnb.chat.entity.ChatMessage;
import com.airdnb.chat.entity.ChatRoom;
import com.airdnb.chat.entity.MessageType;
import com.airdnb.chat.repository.ChatMessageRepository;
import com.airdnb.member.MemberService;
import com.airdnb.member.entity.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final MemberService memberService;
    private final MemberChatRoomService memberChatRoomService;
    private final ChatRoomService chatRoomService;
    private final ChatMessageRepository chatMessageRepository;

    private final Map<Long, Queue<ChatMessage>> messageBuffers = new ConcurrentHashMap<>();

    @Transactional
    public MessageResponse createChat(MessageCreation messageCreation) {
        Member member = memberService.findMemberById(messageCreation.getMemberId());
        ChatRoom chatRoom = chatRoomService.findChatRoomById(messageCreation.getRoomId());
        boolean isMemberInChatRoom = memberChatRoomService.existsByMemberId(messageCreation.getMemberId());
        String message = "";

        if (messageCreation.getMessageType().equals(MessageType.ENTER) && isMemberInChatRoom) {
            log.info("채팅방 입장");
        }

        if (messageCreation.getMessageType().equals(MessageType.LEAVE)) {
            log.info("채팅방 퇴장");
            chatRoomService.softDeleteChatRoom(chatRoom.getId());
        }

        if (messageCreation.getMessageType().equals(MessageType.CHAT)) {
            log.info("채팅 메시지 전송");
            message = messageCreation.getContent();
        }

        ChatMessage chatMessage = ChatMessage.builder()
            .userName(member.getName())
            .content(message)
            .chatRoom(chatRoom)
            .build();

        messageBuffers.computeIfAbsent(chatRoom.getId(), k -> new ConcurrentLinkedQueue<>()).add(chatMessage);

        log.info(chatMessage.getContent());
        return MessageResponse.from(chatMessage);

    }

    @Transactional
    @Scheduled(fixedDelay = 21000)
    public void saveMessages() {
        messageBuffers.forEach((roomId, buffer) -> {
            List<ChatMessage> messages = new ArrayList<>(buffer);
            buffer.clear();
            chatMessageRepository.saveAll(messages);
            log.warn("{}개의 메시지가 저장되었습니다.", messages.size());
        });
    }

}
