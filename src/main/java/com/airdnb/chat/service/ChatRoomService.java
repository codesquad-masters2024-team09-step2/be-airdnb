package com.airdnb.chat.service;

import com.airdnb.chat.dto.ChatRoomRecipient;
import com.airdnb.chat.entity.ChatMessage;
import com.airdnb.chat.entity.ChatRoom;
import com.airdnb.chat.entity.MemberChatRoom;
import com.airdnb.chat.repository.ChatRoomRepository;
import com.airdnb.global.exception.NotFoundException;
import com.airdnb.member.MemberService;
import com.airdnb.member.entity.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberService memberService;
    private final MemberChatRoomService memberChatRoomService;

    public ChatRoom findChatRoomById(Long id) {
        return chatRoomRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));
    }

    public Long createChatRoom(ChatRoomCreation chatRoomCreation) {
        Member host = memberService.findMemberById(chatRoomCreation.getHostId());
        Member guest = memberService.findMemberById(chatRoomCreation.getGuestId());
        List<Member> members = List.of(host, guest);

        ChatRoom chatRoom = ChatRoom.builder()
            .host(host)
            .guest(guest)
            .build();

        chatRoomRepository.save(chatRoom);

        memberChatRoomService.createMemberChatRoom(members, chatRoom);
        return chatRoom.getId();
    }

    public void softDeleteChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = findChatRoomById(chatRoomId);
        chatRoom.softDelete();
    }

    public List<ChatRoomRecipient> getChatRooms() {

        Member member = memberService.findMemberById(memberService.getCurrentMemberId());

        List<ChatRoomRecipient> chatRooms = member.getMemberChatRooms().stream()
            .map(MemberChatRoom::getChatRoom)
            .map(chatRoom -> ChatRoomRecipient.from(chatRoom, chatRoom.getRecipientName(member)))
            .toList();

        if (chatRooms.isEmpty()) {
            throw new NotFoundException("채팅방이 존재하지 않습니다.");
        }

        return chatRooms;
    }

    public List<ChatMessage> getMessagesFromRoom(Long roomId) {
        return findChatRoomById(roomId).getMessages();

    }


}
