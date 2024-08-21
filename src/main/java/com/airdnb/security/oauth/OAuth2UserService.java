package com.airdnb.security.oauth;

import com.airdnb.member.MemberRepository;
import com.airdnb.member.entity.Member;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        Map<String, Object> attributes = super.loadUser(userRequest).getAttributes();

        OAuthUserInfo oAuthUserInfo = OAuthUserInfo.ofGoogle(attributes);

        oauthVerify(oAuthUserInfo);

        return super.loadUser(userRequest);
    }

    private void oauthVerify(OAuthUserInfo oAuthUserInfo) {
        if (memberRepository.existsById(oAuthUserInfo.getEmail())) {
            log.info("이미 존재하는 id입니다.");
            return;
        }
        Member member = oAuthUserInfo.toEntity();
        memberRepository.save(member);
    }
}
