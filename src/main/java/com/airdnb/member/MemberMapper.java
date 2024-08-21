package com.airdnb.member;

import com.airdnb.member.dto.MemberRegistration;
import com.airdnb.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public Member toMember(MemberRegistration memberRegistration) {
        return Member.builder()
            .id(memberRegistration.getId())
            .name(memberRegistration.getName())
            .password(memberRegistration.getPassword())
            .role(memberRegistration.getRole())
            .build();
    }

}
