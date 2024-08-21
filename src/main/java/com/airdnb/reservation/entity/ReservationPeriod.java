package com.airdnb.reservation.entity;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class ReservationPeriod {
    private LocalDateTime checkinAt;
    private LocalDateTime checkoutAt;

    public long getDaysOfStay() {
        LocalDate checkinDate = checkinAt.toLocalDate();
        LocalDate checkoutDate = checkoutAt.toLocalDate();
        return ChronoUnit.DAYS.between(checkinDate, checkoutDate);
    }

    public List<LocalDate> getReservationDates() {
        List<LocalDate> reservationDates = new ArrayList<>();
        LocalDate checkinDate = checkinAt.toLocalDate();
        LocalDate checkoutDate = checkoutAt.toLocalDate();

        while (checkinDate.isBefore(checkoutDate)) {
            reservationDates.add(checkinDate);
            checkinDate = checkinDate.plusDays(1);
        }
        return reservationDates;
    }
}
