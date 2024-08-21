package com.airdnb.chat.service;

import com.airdnb.chat.entity.ChatRoom;
import com.airdnb.chat.entity.MemberChatRoom;
import com.airdnb.chat.repository.MemberChatRoomRepository;
import com.airdnb.member.entity.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberChatRoomService {

    private final MemberChatRoomRepository memberChatRoomRepository;

    public boolean existsByMemberId(String memberId) {
        return memberChatRoomRepository.existsByMemberId(memberId);
    }

    public void createMemberChatRoom(List<Member> members, ChatRoom chatRoom) {
        for (Member member : members) {
            MemberChatRoom memberChatRoom = MemberChatRoom.builder()
                .member(member)
                .chatRoom(chatRoom)
                .build();
            memberChatRoomRepository.save(memberChatRoom);
        }
    }


}
