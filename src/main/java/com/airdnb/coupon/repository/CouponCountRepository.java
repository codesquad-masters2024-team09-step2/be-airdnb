package com.airdnb.coupon.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CouponCountRepository {

    private static final String COUPON_COUNT_KEY = "coupon_count";

    private final RedisTemplate<String, String> redisTemplate;


    public Long increment() {
        return redisTemplate
                .opsForValue()
                .increment(COUPON_COUNT_KEY);
    }

    public Long size() {
        return redisTemplate.opsForValue()
                .size(COUPON_COUNT_KEY);
    }

    public void clear() {
        redisTemplate.delete(COUPON_COUNT_KEY);
    }
}
