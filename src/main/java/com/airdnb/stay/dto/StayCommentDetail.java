package com.airdnb.stay.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class StayCommentDetail {
    private final Long id;
    private final String writer;
    private final String content;
    private final LocalDateTime createdAt;
    private final Float rating;
}
