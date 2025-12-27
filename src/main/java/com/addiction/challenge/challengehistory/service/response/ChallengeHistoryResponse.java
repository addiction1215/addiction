package com.addiction.challenge.challengehistory.service.response;

import com.addiction.challenge.challange.entity.Challenge;
import com.addiction.challenge.challengehistory.entity.ChallengeHistory;
import com.addiction.common.enums.ChallengeStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChallengeHistoryResponse {
    private final Long challengeHistoryId;
    private final String title;
    private final String content;
    private final String badge;
    private final Integer reward;
    private final ChallengeStatus status;

    @Builder
    public ChallengeHistoryResponse(Long challengeHistoryId, String title, String content, String badge, Integer reward, ChallengeStatus status) {
        this.challengeHistoryId = challengeHistoryId;
        this.title = title;
        this.content = content;
        this.badge = badge;
        this.status = status;
        this.reward = reward;
    }

    public static ChallengeHistoryResponse createResponse(ChallengeHistory challengeHistory) {
        Challenge challenge = challengeHistory.getChallenge();
        return ChallengeHistoryResponse.builder()
                .challengeHistoryId(challengeHistory.getId())
                .title(challenge.getTitle())
                .content(challenge.getContent())
                .badge(challenge.getBadge())
                .reward(challenge.getReward())
                .status(challengeHistory.getStatus())
                .build();
    }

}
