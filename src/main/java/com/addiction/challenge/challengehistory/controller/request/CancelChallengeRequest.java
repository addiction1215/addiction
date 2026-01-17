package com.addiction.challenge.challengehistory.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * 챌린지 포기 요청
 */
@Getter
public class CancelChallengeRequest {

    @NotNull(message = "챌린지 이력 ID는 필수입니다")
    private final Long challengeHistoryId;

    public CancelChallengeRequest(Long challengeHistoryId) {
        this.challengeHistoryId = challengeHistoryId;
    }
}
