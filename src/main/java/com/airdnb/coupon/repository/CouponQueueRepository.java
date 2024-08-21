package com.airdnb.coupon.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CouponQueueRepository {
    private static final String COUPON_QUEUE_KEY = "coupon_queue";

    private final RedisTemplate<String, String> redisTemplate;

    public Long enqueue(String memberId) {
        return redisTemplate.opsForList().rightPush(COUPON_QUEUE_KEY, memberId);
    }

    public String dequeue() {
        return redisTemplate.opsForList().leftPop(COUPON_QUEUE_KEY);
    }

    public Long getPosition(String memberId) {
        return redisTemplate.opsForList().indexOf(COUPON_QUEUE_KEY, memberId);
    }

    public Long size() {
        return redisTemplate.opsForList().size(COUPON_QUEUE_KEY);
    }

    public void clear() {
        redisTemplate.delete(COUPON_QUEUE_KEY);
    }
}
