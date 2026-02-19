package com.addiction.challenge.challengehistory.service.response;

import com.addiction.challenge.challange.entity.Challenge;
import com.addiction.challenge.challengehistory.entity.ChallengeHistory;
import com.addiction.challenge.challengehistory.entity.ChallengeStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FinishedChallengeHistoryResponse {
    private final Long challengeHistoryId;
    private final String title;
    private final String content;
    private final String badge;
    private final Integer reward;
    private final ChallengeStatus status;

    @Builder
    public FinishedChallengeHistoryResponse(Long challengeHistoryId, String title, String content, String badge, Integer reward, ChallengeStatus status) {
        this.challengeHistoryId = challengeHistoryId;
        this.title = title;
        this.content = content;
        this.badge = badge;
        this.reward = reward;
        this.status = status;
    }

    public static FinishedChallengeHistoryResponse createResponse(ChallengeHistory challengeHistory, String badge) {
        Challenge challenge = challengeHistory.getChallenge();
        return FinishedChallengeHistoryResponse.builder()
                .challengeHistoryId(challengeHistory.getId())
                .title(challenge.getTitle())
                .content(challenge.getContent())
                .badge(badge)
                .reward(challenge.getReward())
                .status(challengeHistory.getStatus())
                .build();
    }
}
