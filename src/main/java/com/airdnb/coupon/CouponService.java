package com.airdnb.coupon;

import com.airdnb.coupon.dto.CouponDetail;
import com.airdnb.coupon.dto.CouponRequestResultDetail;
import com.airdnb.coupon.entity.Coupon;
import com.airdnb.coupon.entity.CouponRequestResult;
import com.airdnb.coupon.entity.CouponStatus;
import com.airdnb.coupon.repository.AppliedMemberRepository;
import com.airdnb.coupon.repository.CouponCountRepository;
import com.airdnb.coupon.repository.CouponQueueRepository;
import com.airdnb.coupon.repository.CouponRepository;
import com.airdnb.coupon.repository.CouponRequestResultRepository;
import com.airdnb.global.exception.InvalidRequestException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CouponService {
    private static final int MAX_CREATE_COUNT = 100;
    private static final double DISCOUNT_RATE = 50;

    private final CouponRepository couponRepository;
    private final CouponCountRepository couponCountRepository;
    private final AppliedMemberRepository appliedMemberRepository;
    private final CouponQueueRepository couponQueueRepository;
    private final CouponRequestResultRepository couponRequestResultRepository;

    public Long requestCoupon(String memberId) {
        if (couponCountRepository.size() >= MAX_CREATE_COUNT) {
            throw new InvalidRequestException("쿠폰 발급이 종료되었습니다.");
        }

        Long queueSize = couponQueueRepository.enqueue(memberId);
        log.debug("대기큐에 멤버가 추가되었습니다. member = {}, size = {}", memberId, queueSize);
        return queueSize;
    }

    @Scheduled(fixedRate = 1000) // 작업 속도 및 한번에 처리할 인원은 여기서 조절
    public void processQueue() {

        for (int i = 0; i < 50; i++) {
            String memberId = couponQueueRepository.dequeue();
            if (memberId != null) {
                log.debug("작업이 시작됩니다 member = {}", memberId);
                CouponRequestResult couponRequestResult = processCouponRequest(memberId);
                couponRequestResultRepository.save(couponRequestResult);
                log.debug("작업이 끝났습니다. size = {}", couponQueueRepository.size());
            }
        }
    }

    public Long createCoupon(String memberId) {
        Long added = appliedMemberRepository.add(memberId);
        if (added != 1) {
            throw new InvalidRequestException("이미 쿠폰이 발급된 사용자입니다.");
        }

        Long couponCount = couponCountRepository.increment();

        if (couponCount > MAX_CREATE_COUNT) {
            throw new InvalidRequestException("발급 가능한 쿠폰이 모두 소진되었습니다.");
        }

        Coupon coupon = new Coupon(DISCOUNT_RATE, CouponStatus.ACTIVE, memberId);
        couponRepository.save(coupon);
        return coupon.getId();
    }

    @Transactional(readOnly = true)
    public Long getQueuePosition(String memberId) {
        return couponQueueRepository.getPosition(memberId);
    }

    @Transactional(readOnly = true)
    public CouponRequestResultDetail getCouponRequestResult(String memberId) {
        List<CouponRequestResult> couponRequestResults = couponRequestResultRepository.findAllByMemberId(memberId);
        if (couponRequestResults.isEmpty()) {
            throw new InvalidRequestException("해당 사용자에 대한 쿠폰 발급 요청 결과가 존재하지 않습니다.");
        }
        CouponRequestResult latest = couponRequestResults.get(couponRequestResults.size() - 1);
        return CouponRequestResultDetail.from(latest);
    }

    @Transactional(readOnly = true)
    public List<CouponDetail> queryCoupons(String memberId) {
        List<Coupon> coupons = couponRepository.findAllByMemberId(memberId);
        return coupons.stream()
                .map(CouponDetail::from)
                .toList();
    }

    private CouponRequestResult processCouponRequest(String memberId) {
        try {
            Long couponId = createCoupon(memberId);
            return new CouponRequestResult(memberId, true, couponId, null);
        } catch (InvalidRequestException e) {
            return new CouponRequestResult(memberId, false, null, e.getMessage());
        }
    }
}
