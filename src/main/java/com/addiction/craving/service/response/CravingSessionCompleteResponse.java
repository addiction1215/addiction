package com.addiction.craving.service.response;

import com.addiction.craving.entity.CravingSession;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CravingSessionCompleteResponse {
    private final Long id;
    private final LocalDateTime completedAt;
    private final long todayCompletedCount;

    @Builder
    public CravingSessionCompleteResponse(Long id, LocalDateTime completedAt, long todayCompletedCount) {
        this.id = id;
        this.completedAt = completedAt;
        this.todayCompletedCount = todayCompletedCount;
    }

    public static CravingSessionCompleteResponse of(
            CravingSession session,
            long todayCompletedCount
    ) {
        return CravingSessionCompleteResponse.builder()
                .id(session.getId())
                .completedAt(session.getCompletedAt())
                .todayCompletedCount(todayCompletedCount)
                .build();
    }
}
