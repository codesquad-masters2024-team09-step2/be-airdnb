package com.airdnb.stay.dto;

import com.airdnb.stay.entity.StayType;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class StayListQuery {
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
}
