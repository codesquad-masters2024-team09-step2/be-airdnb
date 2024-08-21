package com.airdnb.coupon.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CouponRequestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memberId;
    private boolean processed;
    private Long couponId;
    private String failureReason;

    public CouponRequestResult(String memberId, boolean processed, Long couponId, String failureReason) {
        this.memberId = memberId;
        this.processed = processed;
        this.couponId = couponId;
        this.failureReason = failureReason;
    }
}