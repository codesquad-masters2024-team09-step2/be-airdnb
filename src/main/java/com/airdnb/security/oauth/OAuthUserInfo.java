package com.airdnb.security.oauth;

import com.airdnb.member.entity.Member;
import java.util.Map;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OAuthUserInfo {

    String name;
    String email;
    String profile;


    public static OAuthUserInfo ofGoogle(Map<String, Object> attributes) {
        return OAuthUserInfo.builder()
            .name((String) attributes.get("name"))
            .email((String) attributes.get("email"))
            .profile((String) attributes.get("picture"))
            .build();
    }

    public Member toEntity() {
        return Member.builder()
            .id(email)
            .name(name)
            .role("guest")
            .build();
    }
}
