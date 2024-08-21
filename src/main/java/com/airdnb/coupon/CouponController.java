package com.airdnb.coupon;

import com.airdnb.coupon.dto.CouponDetail;
import com.airdnb.coupon.dto.CouponRequestResultDetail;
import com.airdnb.global.ApiResponse;
import com.airdnb.security.SecurityMemberProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
public class CouponController {
    private final CouponService couponService;

    @PostMapping
    public ApiResponse createCoupon() {
        String currentMemberId = SecurityMemberProvider.getCurrentMemberId();

        Long position = couponService.requestCoupon(currentMemberId);

        return ApiResponse.success(position);
    }

    @GetMapping("/queue")
    public ApiResponse getQueuePosition() {
        String currentMemberId = SecurityMemberProvider.getCurrentMemberId();
        Long position = couponService.getQueuePosition(currentMemberId);
        if (position == null) {
            return ApiResponse.success("대기열에 존재하지 않습니다.");
        }
        return ApiResponse.success(position);
    }

    @GetMapping("/result")
    public ApiResponse getCouponRequestResult() {
        String currentMemberId = SecurityMemberProvider.getCurrentMemberId();
        CouponRequestResultDetail result = couponService.getCouponRequestResult(currentMemberId);
        System.out.println(result);

        return ApiResponse.success(result);
    }

    @GetMapping
    public ApiResponse queryCurrentMemberCoupons() {
        String currentMemberId = SecurityMemberProvider.getCurrentMemberId();
        List<CouponDetail> coupons = couponService.queryCoupons(currentMemberId);

        return ApiResponse.success(coupons);
    }
}
