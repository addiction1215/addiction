package com.addiction.craving.service.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TodayCravingSummaryResponse {
    private final long completedCount;

    @Builder
    public TodayCravingSummaryResponse(long completedCount) {
        this.completedCount = completedCount;
    }

    public static TodayCravingSummaryResponse of(long completedCount) {
        return TodayCravingSummaryResponse.builder()
                .completedCount(completedCount)
                .build();
    }
}
