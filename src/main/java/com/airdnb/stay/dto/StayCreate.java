package com.airdnb.stay.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StayCreate {
    private final String name;
    private final Integer price;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final Long imageId;
    private final List<Long> tagIds;
    private final Integer maxGuests;
    private final String address;
    private final Double latitude;
    private final Double longitude;
    private final String type;
}
