package com.airdnb.security.jwt;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtBlacklistService {

    private static final String BLACKLISTED = "BLACKLISTED: ";

    private final RedisTemplate<String, String> redisTemplate;


    public void blacklistToken(String token, long expirationTime) {
        redisTemplate.opsForValue().set(BLACKLISTED + token, "blacklisted", expirationTime, TimeUnit.MILLISECONDS);
        log.info("토큰 블랙리스트 추가: {}", token);
    }

    public boolean isTokenBlacklisted(String token) {
        return redisTemplate.hasKey(BLACKLISTED + token);
    }
}
