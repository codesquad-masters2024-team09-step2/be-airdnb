package com.airdnb.reservation.entity;

import com.airdnb.global.exception.NotFoundException;
import java.util.Arrays;

public enum ReservationStatus {
    PENDING, APPROVED, REJECTED, CANCELED;

    public static ReservationStatus of(String statusValue) {
        return Arrays.stream(values())
                .filter(reservationStatus -> reservationStatus.name().equals(statusValue.toUpperCase()))
                .findAny()
                .orElseThrow(() -> new NotFoundException("일치하는 예약 상태를 찾을 수 없습니다."));
    }
}