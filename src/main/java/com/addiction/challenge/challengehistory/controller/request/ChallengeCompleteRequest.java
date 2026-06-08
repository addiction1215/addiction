package com.addiction.challenge.challengehistory.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChallengeCompleteRequest {

    @NotNull(message = "챌린지 이력 ID는 필수입니다")
    private Long challengeHistoryId;

    @Builder
    public ChallengeCompleteRequest(Long challengeHistoryId) {
        this.challengeHistoryId = challengeHistoryId;
    }
}
