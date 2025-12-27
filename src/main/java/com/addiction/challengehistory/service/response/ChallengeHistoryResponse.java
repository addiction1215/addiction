package com.addiction.challengehistory.service.response;

import com.addiction.challenge.entity.Challenge;
import com.addiction.challengehistory.entity.ChallengeHistory;
import com.addiction.common.enums.ChallengeStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChallengeHistoryResponse {
    private final Long challengeId;
    private final String title;
    private final String content;
    private final String badge;
    private final ChallengeStatus status;

    @Builder
    public ChallengeHistoryResponse(Long challengeId, String title, String content, String badge, ChallengeStatus status) {
        this.challengeId = challengeId;
        this.title = title;
        this.content = content;
        this.badge = badge;
        this.status = status;
    }

    public static ChallengeHistoryResponse createResponse(ChallengeHistory challengeHistory) {
        Challenge challenge = challengeHistory.getChallenge();
        return ChallengeHistoryResponse.builder()
                .challengeId(challenge.getId())
                .title(challenge.getTitle())
                .content(challenge.getContent())
                .badge(challenge.getBadge())
                .status(challengeHistory.getStatus())
                .build();
    }

    public static ChallengeHistoryResponse fromChallenge(Challenge challenge) {
        return ChallengeHistoryResponse.builder()
                .challengeId(challenge.getId())
                .title(challenge.getTitle())
                .content(challenge.getContent())
                .badge(challenge.getBadge())
                .status(ChallengeStatus.LEFT)
                .build();
    }
}
