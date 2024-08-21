package com.airdnb.stay.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StayDetailQueryResponse {
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

    public static StayDetailQueryResponse from(StayDetailQuery stayDetailQuery) {
        return StayDetailQueryResponse.builder()
                .id(stayDetailQuery.getId())
                .name(stayDetailQuery.getName())
                .price(stayDetailQuery.getPrice())
                .startDate(stayDetailQuery.getStartDate())
                .endDate(stayDetailQuery.getEndDate())
                .maxGuests(stayDetailQuery.getMaxGuests())
                .address(stayDetailQuery.getAddress())
                .latitude(stayDetailQuery.getLatitude())
                .longitude(stayDetailQuery.getLongitude())
                .hostName(stayDetailQuery.getHostName())
                .imageUrl(stayDetailQuery.getImageUrl())
                .type(stayDetailQuery.getType())
                .rating(stayDetailQuery.getRating())
                .tagNames(stayDetailQuery.getTagNames())
                .comments(stayDetailQuery.getComments())
                .closedDates(stayDetailQuery.getClosedDates())
                .build();
    }
}
