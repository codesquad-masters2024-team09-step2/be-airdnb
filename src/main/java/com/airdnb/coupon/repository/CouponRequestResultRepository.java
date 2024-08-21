package com.airdnb.coupon.repository;

import com.airdnb.coupon.entity.CouponRequestResult;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRequestResultRepository extends JpaRepository<CouponRequestResult, Long> {
    List<CouponRequestResult> findAllByMemberId(String memberId);
}
