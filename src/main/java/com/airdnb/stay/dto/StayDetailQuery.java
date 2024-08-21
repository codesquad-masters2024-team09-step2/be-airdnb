package com.airdnb.stay.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StayDetailQuery {
    private final Long id;
    private final String name;
    private final Integer price;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final Integer maxGuests;
    private final String address;
    private final Double latitude;
    private final Double longitude;
    private final String hostName;
    private final String imageUrl;
    private final String type;
    private final Double rating;
    private final List<String> tagNames;
    private final List<StayCommentDetail> comments;
    private final List<LocalDate> closedDates;
}
