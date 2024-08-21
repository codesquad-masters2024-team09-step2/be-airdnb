package com.airdnb.chat.repository;

import com.airdnb.chat.entity.MemberChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberChatRoomRepository extends JpaRepository<MemberChatRoom, Long> {

    boolean existsByMemberId(String memberId);

}
