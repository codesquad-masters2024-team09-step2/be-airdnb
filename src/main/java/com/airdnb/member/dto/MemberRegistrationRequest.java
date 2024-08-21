package com.airdnb.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MemberRegistrationRequest {

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @NotBlank
    String id;
    @NotBlank
    String password;
    @NotBlank
    String name;
    @NotBlank
    String role;

}
