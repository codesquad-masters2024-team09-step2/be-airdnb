package com.airdnb.member;

import com.airdnb.global.exception.NotFoundException;
import com.airdnb.member.dto.MemberRegistration;
import com.airdnb.member.dto.MemberVerification;
import com.airdnb.member.dto.VerificationResponse;
import com.airdnb.member.entity.Member;
import com.airdnb.security.jwt.JwtUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberMapper memberMapper;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public Member findMemberById(String id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("id와 일치하는 유저가 존재하지 않습니다."));
    }

    @Transactional
    public void register(MemberRegistration memberRegistration) {
        if (memberRepository.existsById(memberRegistration.getId())) {
            throw new IllegalArgumentException("이미 존재하는 id입니다.");
        }

        if (memberRepository.existsByName(memberRegistration.getName())) {
            throw new IllegalArgumentException("이미 존재하는 이름입니다.");
        }

        Member member = memberMapper.toMember(memberRegistration);
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberRepository.save(member);
    }

    @Transactional
    public VerificationResponse verify(MemberVerification memberVerification) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                memberVerification.getId(),
                memberVerification.getPassword()
            )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Member member = findMemberById(memberVerification.getId());
        String token = jwtUtil.createToken(member.getId());
        log.info("토큰 발급 완료: {}", token);

        return VerificationResponse.from(token, member);
    }

    public String getCurrentMemberId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public List<String> getCurrentMemberRoles() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();
    }


}




