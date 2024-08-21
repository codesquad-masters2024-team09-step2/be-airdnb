package com.airdnb.member.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MemberRegistration {

    String id;
    String password;
    String name;
    String role;

    public static MemberRegistration from(MemberRegistrationRequest registrationRequest) {
        return MemberRegistration.builder()
            .id(registrationRequest.getId())
            .password(registrationRequest.getPassword())
            .name(registrationRequest.getName())
            .role(registrationRequest.getRole())
            .build();
    }

}
