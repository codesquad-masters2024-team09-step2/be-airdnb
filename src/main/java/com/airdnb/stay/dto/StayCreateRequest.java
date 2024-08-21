package com.airdnb.stay.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StayCreateRequest {
    @NotBlank
    private final String name;
    @NotNull
    private final Integer price;
    @NotNull
    private final LocalDateTime startDate;
    @NotNull
    private final LocalDateTime endDate;
    @NotNull
    private final Long imageId;
    private final List<Long> tagIds;
    @NotNull
    private final Integer maxGuests;
    @NotBlank
    private final String address;
    @NotNull
    @DecimalMin(value = "-90.0", message = "위도는 -90.0 이상이어야 합니다.")
    @DecimalMax(value = "90.0", message = "위도는 90.0 이하여야 합니다.")
    private final Double latitude;
    @NotNull
    @DecimalMin(value = "-180.0", message = "경도는 -180.0 이상이어야 합니다.")
    @DecimalMax(value = "180.0", message = "경도는 180.0 이하여야 합니다.")
    private final Double longitude;
    @NotBlank
    private final String type;

    public StayCreate toStayCreate() {
        return new StayCreate(
                name
                , price
                , startDate
                , endDate
                , imageId
                , tagIds
                , maxGuests
                , address
                , latitude
                , longitude,
                type);
    }
}
