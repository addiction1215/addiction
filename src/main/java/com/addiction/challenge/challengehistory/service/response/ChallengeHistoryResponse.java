package com.addiction.challenge.challengehistory.service.response;

import com.addiction.challenge.challange.entity.Challenge;
import com.addiction.challenge.challengehistory.entity.ChallengeHistory;
import com.addiction.challenge.challengehistory.entity.ChallengeStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChallengeHistoryResponse {
    private final Long challengeHistoryId;
    private final String title;
    private final String content;
    private final String badge;
    private final Integer reward;
    private final Integer progress;
    private final ChallengeStatus status;
    private final Boolean completionAvailable;

    @Builder
    public ChallengeHistoryResponse(Long challengeHistoryId, String title, String content, String badge, Integer reward, Integer progress, ChallengeStatus status, Boolean completionAvailable) {
        this.challengeHistoryId = challengeHistoryId;
        this.title = title;
        this.content = content;
        this.badge = badge;
        this.status = status;
        this.reward = reward;
        this.progress = progress;
        this.completionAvailable = completionAvailable != null
                ? completionAvailable
                : status == ChallengeStatus.PROGRESSING && progress != null && progress == 100;
    }

    public static ChallengeHistoryResponse createResponse(ChallengeHistory challengeHistory, String badge, Integer progress) {
        Challenge challenge = challengeHistory.getChallenge();
        return ChallengeHistoryResponse.builder()
                .challengeHistoryId(challengeHistory.getId())
                .title(challenge.getTitle())
                .content(challenge.getContent())
                .badge(badge)
                .reward(challenge.getReward())
                .progress(progress)
                .status(challengeHistory.getStatus())
                .build();
    }

}
