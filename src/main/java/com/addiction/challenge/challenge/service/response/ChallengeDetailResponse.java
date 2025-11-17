package com.addiction.challenge.challenge.service.response;

import com.addiction.challenge.challenge.entity.Challenge;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChallengeDetailResponse {

    private final String badge;
    private final String title;
    private final String content;

    @Builder
    public ChallengeDetailResponse(String badge, String title, String content) {
        this.badge = badge;
        this.title = title;
        this.content = content;
    }

    public static ChallengeDetailResponse createResponse(Challenge challenge) {
        return ChallengeDetailResponse.builder()
                .badge(challenge.getBadge())
                .title(challenge.getTitle())
                .content(challenge.getContent())
                .build();
    }
}
