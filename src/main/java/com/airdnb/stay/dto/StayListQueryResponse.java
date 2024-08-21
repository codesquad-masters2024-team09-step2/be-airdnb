package com.airdnb.stay.dto;

import com.airdnb.stay.entity.StayType;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class StayListQueryResponse {
    private final Long id;
    private final String name;
    private final Integer price;
    private final Integer maxGuests;
    private final String address;
    private final Double latitude;
    private final Double longitude;
    private final String imageUrl;
    private final StayType type;
    private final Double rating;
    private final Integer commentCount;
    private final List<String> tagNames;

    public static StayListQueryResponse from(StayListQuery stayListQuery) {
        return StayListQueryResponse.builder()
                .id(stayListQuery.getId())
                .name(stayListQuery.getName())
                .price(stayListQuery.getPrice())
                .maxGuests(stayListQuery.getMaxGuests())
                .address(stayListQuery.getAddress())
                .latitude(stayListQuery.getLatitude())
                .longitude(stayListQuery.getLongitude())
                .imageUrl(stayListQuery.getImageUrl())
                .type(stayListQuery.getType())
                .rating(stayListQuery.getRating())
                .commentCount(stayListQuery.getCommentCount())
                .tagNames(stayListQuery.getTagNames())
                .build();
    }
}
