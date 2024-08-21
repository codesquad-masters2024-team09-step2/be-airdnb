package com.airdnb.reservation.dto;

import com.airdnb.reservation.entity.ReservationStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@Getter
public class ReservationQueryResponse {
    private final Long id;
    private final String stayName;
    private final String hostName;
    private final String customerName;
    private final ReservationStatus status;
    private final LocalDateTime checkinAt;
    private final LocalDateTime checkoutAt;
    private final LocalDateTime createdAt;
    private final Integer guestCount;
    private final Double paymentAmount;

    public static ReservationQueryResponse from(ReservationQuery reservationQuery) {
        return ReservationQueryResponse.builder()
                .id(reservationQuery.getId())
                .hostName(reservationQuery.getHostName())
                .paymentAmount(reservationQuery.getPaymentAmount())
                .checkinAt(reservationQuery.getCheckinAt())
                .checkoutAt(reservationQuery.getCheckoutAt())
                .createdAt(reservationQuery.getCreatedAt())
                .stayName(reservationQuery.getStayName())
                .guestCount(reservationQuery.getGuestCount())
                .customerName(reservationQuery.getCustomerName())
                .status(reservationQuery.getStatus())
                .build();
    }
}
