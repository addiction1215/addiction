package com.addiction.craving.service.response;

import com.addiction.craving.entity.CravingSession;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CravingSessionResponse {
    private final Long id;
    private final LocalDateTime startedAt;
    private final int durationSeconds;

    @Builder
    public CravingSessionResponse(Long id, LocalDateTime startedAt, int durationSeconds) {
        this.id = id;
        this.startedAt = startedAt;
        this.durationSeconds = durationSeconds;
    }

    public static CravingSessionResponse from(CravingSession session) {
        return CravingSessionResponse.builder()
                .id(session.getId())
                .startedAt(session.getStartedAt())
                .durationSeconds(CravingSession.HOLD_DURATION_SECONDS)
                .build();
    }
}
