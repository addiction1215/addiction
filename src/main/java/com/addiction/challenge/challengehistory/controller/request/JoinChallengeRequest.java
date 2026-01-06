package com.addiction.challenge.challengehistory.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * 챌린지 참여 요청
 */
@Getter
public class JoinChallengeRequest {

    @NotNull(message = "챌린지 ID는 필수입니다")
    private final Long challengeId;

    public JoinChallengeRequest(Long challengeId) {
        this.challengeId = challengeId;
    }
}
