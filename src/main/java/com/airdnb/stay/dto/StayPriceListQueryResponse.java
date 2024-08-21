package com.airdnb.stay.dto;

import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class StayPriceListQueryResponse {
    private final Map<Integer, Long> countPerPrice;
    private final Integer minPrice;
    private final Integer maxPrice;
    private final Integer avgPrice;

    public static StayPriceListQueryResponse from(StayPriceListQuery stayPriceListQuery) {
        return new StayPriceListQueryResponse(
                stayPriceListQuery.getCountPerPrice()
                , stayPriceListQuery.getMinPrice()
                , stayPriceListQuery.getMaxPrice()
                , stayPriceListQuery.getAvgPrice());
    }
}
