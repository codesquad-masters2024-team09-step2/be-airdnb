package com.airdnb.coupon.dto;

import com.airdnb.coupon.entity.Coupon;
import com.airdnb.coupon.entity.CouponStatus;
import lombok.Value;

@Value
public class CouponDetail {
    Long id;

    Double discountRate;

    CouponStatus status;

    String memberId;

    public static CouponDetail from(Coupon coupon) {
        return new CouponDetail(coupon.getId(), coupon.getDiscountRate(), coupon.getStatus(), coupon.getMemberId());
    }
}
