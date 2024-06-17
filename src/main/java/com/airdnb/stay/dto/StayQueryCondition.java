package com.airdnb.stay.dto;

import com.airdnb.global.exception.InvalidRequestException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class StayQueryCondition {
    private final LocalDate checkinDate;
    private final LocalDate checkoutDate;
    private final Integer minPrice;
    private final Integer maxPrice;
    private final Integer guestCount;
    private final Double latitude;
    private final Double longitude;
    private final Integer distance;

    public StayQueryCondition(LocalDate checkinDate, LocalDate checkoutDate, Integer minPrice, Integer maxPrice,
                              Integer guestCount, Double latitude, Double longitude, Integer distance) {
        validateDateCondition(checkinDate, checkoutDate);
        validatePriceCondition(minPrice, maxPrice);
        validateLocationCondition(latitude, longitude, distance);

        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.guestCount = guestCount;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
    }

    public List<LocalDate> getReservationDates() {
        List<LocalDate> reservationDates = new ArrayList<>();
        LocalDate checkinDate = this.checkinDate;
        while (checkinDate.isBefore(checkoutDate)) {
            reservationDates.add(checkinDate);
            checkinDate = checkinDate.plusDays(1);
        }
        return reservationDates;
    }

    public boolean hasDateCondition() {
        return checkinDate != null && checkoutDate != null;
    }

    public boolean hasPriceCondition() {
        return minPrice != null && maxPrice != null;
    }

    public boolean hasLocationCondition() {
        return latitude != null && longitude != null && distance != null;
    }

    public boolean hasGuestCountCondition() {
        return guestCount != null;
    }

    private void validateDateCondition(LocalDate checkinDate, LocalDate checkoutDate) {
        if ((checkinDate == null && checkoutDate != null) || (checkinDate != null && checkoutDate == null)) {
            throw new InvalidRequestException("검색 조건이 올바르지 않습니다. (숙박 기간)");
        }
    }

    private void validatePriceCondition(Integer minPrice, Integer maxPrice) {
        if ((minPrice == null && maxPrice != null) || minPrice != null && maxPrice == null) {
            throw new InvalidRequestException("검색 조건이 올바르지 않습니다. (가격)");
        }
    }

    private void validateLocationCondition(Double latitude, Double longitude, Integer distance) {
        if (latitude == null && longitude == null && distance == null) {
            return;
        }
        if (latitude != null && longitude != null && distance != null) {
            return;
        }
        throw new InvalidRequestException("검색 조건이 올바르지 않습니다. (위치) ");
    }
}
