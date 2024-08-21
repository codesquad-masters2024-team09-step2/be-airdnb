package com.airdnb.reservation.entity;

import com.airdnb.global.exception.ForbiddenException;
import com.airdnb.global.exception.InvalidRequestException;
import com.airdnb.member.entity.Member;
import com.airdnb.stay.entity.Stay;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stay_id")
    private Stay stay;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Member customer;
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
    @Embedded
    private ReservationPeriod reservationPeriod;
    private LocalDateTime createdAt;
    private Integer guestCount;
    private Double paymentAmount;

    @Builder
    public Reservation(Stay stay, Member customer, ReservationPeriod reservationPeriod, Integer guestCount,
                       Double paymentAmount) {
        this.stay = stay;
        this.customer = customer;
        this.status = ReservationStatus.PENDING;
        this.reservationPeriod = reservationPeriod;
        this.createdAt = LocalDateTime.now();
        this.guestCount = guestCount;
        this.paymentAmount = paymentAmount;
    }

    public boolean isCustomer(String memberId) {
        return customer.hasSameId(memberId);
    }

    public boolean isHost(String memberId) {
        return stay.hasSameHostId(memberId);
    }

    public void handleReservation(String currentMemberId, ReservationStatus requestStatus) {
        if (this.status != ReservationStatus.PENDING) {
            throw new InvalidRequestException("예약 심사가 가능한 상태가 아닙니다.");
        }
        if (!isHost(currentMemberId)) {
            throw new ForbiddenException("예약 심사 권한이 없는 사용자입니다.");
        }
        this.status = requestStatus;
    }

    public void cancelReservation(String currentMemberId) {
        if (!isHost(currentMemberId) && !isCustomer(currentMemberId)) {
            throw new ForbiddenException("예약 취소 권한이 없는 사용자입니다.");
        }
        if (this.status != ReservationStatus.APPROVED && this.status != ReservationStatus.PENDING) {
            throw new InvalidRequestException("취소 가능한 예약 상태가 아닙니다.");
        }
        stay.removeClosedDate(reservationPeriod.getReservationDates());
        this.status = ReservationStatus.CANCELED;
    }

    public void validateQueryAuthority(String currentMemberId) {
        if (!isCustomer(currentMemberId) && !isHost(currentMemberId)) {
            throw new ForbiddenException("예약 조회 권한이 없는 사용자입니다.");
        }
    }
}
