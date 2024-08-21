package com.airdnb.reservation.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;

@RequiredArgsConstructor
@Getter
public class ReservationCreateRequest {
    @NotNull
    private final Long stayId;
    @NotNull
    private final LocalDateTime checkinAt;
    @NotNull
    private final LocalDateTime checkoutAt;
    @NotNull
    @Range(min = 1, max = 10)
    private final Integer guestCount;

    public ReservationCreate toReservationCreate(String guestId) {
        return new ReservationCreate(stayId, guestId, checkinAt, checkoutAt, guestCount);
    }
}
