package com.addiction.challenge.challengehistory.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 챌린지 참여 요청
 */
@Getter
@NoArgsConstructor
public class ChallengeJoinRequest {

    @NotNull(message = "챌린지 ID는 필수입니다")
    private Long challengeId;

    @Builder
    public ChallengeJoinRequest(Long challengeId) {
        this.challengeId = challengeId;
    }
}
