package com.airdnb.member.dto;


import com.airdnb.member.entity.Member;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class VerificationResponse {

    String accessToken;
    String id;
    String name;
    String role;

    public static VerificationResponse from(String accessToken, Member member) {
        return VerificationResponse.builder()
            .accessToken(accessToken)
            .id(member.getId())
            .name(member.getName())
            .role(member.getRole())
            .build();
    }

}
