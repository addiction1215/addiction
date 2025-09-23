package com.addiction.challenge.service.challenge.response;

import com.addiction.challenge.entity.Challenge;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChallengeResponse {
    private Long challengeId;
    private String title;
    private String content;
    private String badge;

    @Builder
    public ChallengeResponse(Long challengeId, String title, String content, String badge) {
        this.challengeId = challengeId;
        this.title = title;
        this.content = content;
        this.badge = badge;
    }

    public static ChallengeResponse of(Challenge challenge) {
        return ChallengeResponse.builder()
                .challengeId(challenge.getId())
                .title(challenge.getTitle())
                .content(challenge.getContent())
                .badge(challenge.getBadge())
                .build();
    }
}
