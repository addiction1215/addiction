package com.addiction.challenge.challengehistory.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 챌린지 포기 요청
 */
@Getter
@NoArgsConstructor
public class ChallengeCancelRequest {

    @NotNull(message = "챌린지 이력 ID는 필수입니다")
    private Long challengeHistoryId;

    @Builder
    public ChallengeCancelRequest(Long challengeHistoryId) {
        this.challengeHistoryId = challengeHistoryId;
    }
}
