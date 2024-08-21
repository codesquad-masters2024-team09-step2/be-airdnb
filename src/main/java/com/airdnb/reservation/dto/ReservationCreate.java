package com.airdnb.reservation.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ReservationCreate {
    private final Long stayId;
    private final String guestId;
    private final LocalDateTime checkinAt;
    private final LocalDateTime checkoutAt;
    private final Integer guestCount;
}
