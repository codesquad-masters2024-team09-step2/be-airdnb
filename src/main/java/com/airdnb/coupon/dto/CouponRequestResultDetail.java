package com.airdnb.coupon.dto;

import com.airdnb.coupon.entity.CouponRequestResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class CouponRequestResultDetail {
    private Long couponId;
    private boolean processed;
    private String failureReason;

    public CouponRequestResultDetail(Long couponId, boolean processed, String failureReason) {
        this.couponId = couponId;
        this.processed = processed;
        this.failureReason = failureReason;
    }

    public static CouponRequestResultDetail from(CouponRequestResult couponRequestResult) {
        return new CouponRequestResultDetail(couponRequestResult.getCouponId(), couponRequestResult.isProcessed(),
                couponRequestResult.getFailureReason());
    }
}