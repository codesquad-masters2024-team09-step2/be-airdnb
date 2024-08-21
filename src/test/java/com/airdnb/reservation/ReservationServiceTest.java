package com.airdnb.reservation;

import com.airdnb.member.MemberRepository;
import com.airdnb.member.entity.Member;
import com.airdnb.reservation.dto.ReservationCreate;
import com.airdnb.stay.entity.Location;
import com.airdnb.stay.entity.Stay;
import com.airdnb.stay.entity.StayStatus;
import com.airdnb.stay.entity.StayType;
import com.airdnb.stay.repository.StayRepository;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReservationServiceTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ReservationService reservationService;
    @Autowired
    StayRepository stayRepository;

    @Test
    @DisplayName("1000명이 같은 날짜에 예약을 시도")
    void createReservation() throws InterruptedException {
        Member member1 = new Member("a", "123", "a", "abc", null);
        memberRepository.save(member1);
        Member member2 = new Member("ab", "123", "a", "abc", null);
        memberRepository.save(member2);

        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 0, 0);
        Stay stay = new Stay("테스트", 10000, startDate, endDate, 10, new Location("test", 0.0, 0.0), StayStatus.ACTIVE,
                member1, null, StayType.APT);
        stayRepository.save(stay);

        int threadSize = 1000;
        AtomicInteger errorCount = new AtomicInteger();
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadSize);
        ReservationCreate reservationCreate = new ReservationCreate(stay.getId(), "ab",
                LocalDateTime.of(2024, 7, 1, 0, 0),
                LocalDateTime.of(2024, 7, 4, 0, 0), 3);

        for (int i = 0; i < threadSize; i++) {
            executorService.submit(() -> {
                try {
                    reservationService.createReservation(reservationCreate);
                } catch (Exception e) {
                    errorCount.addAndGet(1);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        System.out.println(errorCount);
    }
}