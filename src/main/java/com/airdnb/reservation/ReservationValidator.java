package com.airdnb.reservation;

import com.airdnb.global.exception.InvalidRequestException;
import com.airdnb.reservation.entity.ReservationPeriod;
import com.airdnb.reservation.entity.ReservationStatus;
import com.airdnb.stay.entity.Stay;

public class ReservationValidator {

    public static void validateCustomer(Stay stay, String customerId) {
        if (stay.hasSameHostId(customerId)) {
            throw new InvalidRequestException("예약자와 호스트는 동일할 수 없습니다.");
        }
    }

    public static void validateReservationPeriod(Stay stay, ReservationPeriod reservationPeriod) {
        if (!stay.isSatisfyingPeriod(reservationPeriod)) {
            throw new InvalidRequestException("해당 날짜는 예약이 불가능합니다.");
        }
    }

    public static void validateReservationStatus(ReservationStatus status) {
        if (status == ReservationStatus.PENDING) {
            throw new InvalidRequestException("해당 상태로는 변경할 수 없습니다.");
        }
    }

    public static void validateGuestsCount(Stay stay, Integer guestCount) {
        if (guestCount > stay.getMaxGuests()) {
            throw new InvalidRequestException("예약 신청 인원이 수용 가능 인원을 초과하였습니다.");
        }
    }
}
