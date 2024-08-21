package com.airdnb.coupon.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double discountRate;
    
    @Enumerated(EnumType.STRING)
    private CouponStatus status;

    private String memberId;

    public Coupon(Double discountRate, CouponStatus status, String memberId) {
        this.discountRate = discountRate;
        this.status = status;
        this.memberId = memberId;
    }
}
