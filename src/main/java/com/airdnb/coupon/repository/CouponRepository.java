package com.airdnb.coupon.repository;

import com.airdnb.coupon.entity.Coupon;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    @Query
    List<Coupon> findAllByMemberId(String memberId);
}
