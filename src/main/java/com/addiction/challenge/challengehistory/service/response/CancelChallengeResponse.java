package com.addiction.challenge.challengehistory.service.response;

import lombok.Builder;
import lombok.Getter;

/**
 * 챌린지 포기 응답
 */
@Getter
public class CancelChallengeResponse {
    private final Long challengeHistoryId;

    @Builder
    public CancelChallengeResponse(Long challengeHistoryId) {
        this.challengeHistoryId = challengeHistoryId;
    }

    public static CancelChallengeResponse of(Long challengeHistoryId) {
        return CancelChallengeResponse.builder()
                .challengeHistoryId(challengeHistoryId)
                .build();
    }
}
