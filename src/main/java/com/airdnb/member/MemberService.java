package com.airdnb.member;

import com.airdnb.member.entity.Member;
import com.airdnb.member.dto.MemberRegistration;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberMapper memberMapper;

    public void saveMember(MemberRegistration memberRegistration) {
        Member member = memberMapper.toMember(memberRegistration);
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberRepository.save(member);
    }


}