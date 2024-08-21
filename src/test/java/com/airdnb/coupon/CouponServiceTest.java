package com.airdnb.coupon;

import static org.assertj.core.api.Assertions.assertThat;

import com.airdnb.coupon.repository.AppliedMemberRepository;
import com.airdnb.coupon.repository.CouponCountRepository;
import com.airdnb.coupon.repository.CouponRepository;
import com.airdnb.member.MemberRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CouponServiceTest {
    @Autowired
    CouponRepository couponRepository;
    @Autowired
    CouponCountRepository couponCountRepository;
    @Autowired
    AppliedMemberRepository appliedMemberRepository;
    @Autowired
    CouponService couponService;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        couponRepository.deleteAll();
        couponCountRepository.clear();
        appliedMemberRepository.clear();
    }

    @Test
    void _1000명_접근시_쿠폰_발급() throws InterruptedException {
        int threadCount = 1000;
        AtomicInteger errorCount = new AtomicInteger();
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            String memberId = "member" + i;
            executorService.submit(() -> {
                try {
                    couponService.createCoupon(memberId);
                } catch (Exception e) {
                    errorCount.getAndIncrement();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        long couponCount = couponRepository.count();
        assertThat(couponCount).isEqualTo(100);
        assertThat(errorCount.get()).isEqualTo(900);
    }

    @Test
    void _1명에게_1개의_쿠폰_발급() throws InterruptedException {
        int threadCount = 1000;
        AtomicInteger errorCount = new AtomicInteger();
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            String memberId = "member" + 1;
            executorService.submit(() -> {
                try {
                    couponService.createCoupon(memberId);
                } catch (Exception e) {
                    errorCount.getAndIncrement();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        long couponCount = couponRepository.count();
        assertThat(couponCount).isEqualTo(1);
        assertThat(errorCount.get()).isEqualTo(999);
    }
}