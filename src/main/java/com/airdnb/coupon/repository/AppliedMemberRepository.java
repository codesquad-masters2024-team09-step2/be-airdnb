package com.airdnb.coupon.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class AppliedMemberRepository {
    private static final String APPLIED_MEMBER_KEY = "applied_member";

    private final RedisTemplate<String, String> redisTemplate;

    public Long add(String memberId) {
        return redisTemplate.opsForSet()
                .add(APPLIED_MEMBER_KEY, memberId);
    }

    public void clear() {
        redisTemplate.delete(APPLIED_MEMBER_KEY);
    }
}
