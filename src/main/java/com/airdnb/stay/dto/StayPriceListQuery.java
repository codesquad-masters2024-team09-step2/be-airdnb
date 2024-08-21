package com.airdnb.stay.dto;

import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class StayPriceListQuery {
    private final Map<Integer, Long> countPerPrice;
    private final Integer minPrice;
    private final Integer maxPrice;
    private final Integer avgPrice;
}
