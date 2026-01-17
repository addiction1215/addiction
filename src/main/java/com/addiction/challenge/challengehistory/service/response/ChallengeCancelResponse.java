package com.addiction.challenge.challengehistory.service.response;

import lombok.Builder;
import lombok.Getter;

/**
 * 챌린지 포기 응답
 */
@Getter
public class ChallengeCancelResponse {
    private final Long challengeHistoryId;

    @Builder
    public ChallengeCancelResponse(Long challengeHistoryId) {
        this.challengeHistoryId = challengeHistoryId;
    }

    public static ChallengeCancelResponse of(Long challengeHistoryId) {
        return ChallengeCancelResponse.builder()
                .challengeHistoryId(challengeHistoryId)
                .build();
    }
}
