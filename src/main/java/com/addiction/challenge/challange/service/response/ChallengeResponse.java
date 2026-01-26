package com.addiction.challenge.challange.service.response;

import com.addiction.challenge.challange.entity.Challenge;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChallengeResponse {
    private final Long challengeId;
    private final String title;
    private final String content;
    private final String badge;
    private final Integer reward;

    @Builder
    public ChallengeResponse(Long challengeId, String title, String content, String badge, Integer reward) {
        this.challengeId = challengeId;
        this.title = title;
        this.content = content;
        this.badge = badge;
        this.reward = reward;
    }

    public static ChallengeResponse createResponse(Challenge challenge, String badge) {
        return ChallengeResponse.builder()
                .challengeId(challenge.getId())
                .title(challenge.getTitle())
                .content(challenge.getContent())
                .badge(badge)
                .reward(challenge.getReward())
                .build();
    }
}
