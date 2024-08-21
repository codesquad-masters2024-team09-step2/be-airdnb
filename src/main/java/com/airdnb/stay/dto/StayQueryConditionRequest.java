package com.airdnb.stay.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class StayQueryConditionRequest {
    private final LocalDate checkinDate;
    private final LocalDate checkoutDate;
    private final Integer minPrice;
    private final Integer maxPrice;
    private final Integer guestCount;
    private final Double latitude;
    private final Double longitude;
    private final Integer distance;

    public StayQueryCondition toStayQueryCondition() {
        return new StayQueryCondition(checkinDate, checkoutDate, minPrice, maxPrice, guestCount, latitude, longitude,
                distance);
    }
}
