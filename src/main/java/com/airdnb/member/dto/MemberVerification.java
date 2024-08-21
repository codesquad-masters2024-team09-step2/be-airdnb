package com.airdnb.member.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MemberVerification {

    String id;
    String password;

    public static MemberVerification from(MemberVerificationRequest verificationRequest) {
        return MemberVerification.builder()
            .id(verificationRequest.getId())
            .password(verificationRequest.getPassword())
            .build();
    }
}
