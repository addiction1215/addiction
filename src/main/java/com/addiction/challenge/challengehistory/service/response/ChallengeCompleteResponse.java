package com.addiction.challenge.challengehistory.service.response;

import com.addiction.challenge.challengehistory.entity.ChallengeHistory;
import com.addiction.challenge.challengehistory.entity.ChallengeStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChallengeCompleteResponse {
    private final Long challengeHistoryId;
    private final ChallengeStatus status;

    @Builder
    public ChallengeCompleteResponse(Long challengeHistoryId, ChallengeStatus status) {
        this.challengeHistoryId = challengeHistoryId;
        this.status = status;
    }

    public static ChallengeCompleteResponse of(ChallengeHistory challengeHistory) {
        return ChallengeCompleteResponse.builder()
                .challengeHistoryId(challengeHistory.getId())
                .status(challengeHistory.getStatus())
                .build();
    }
}
