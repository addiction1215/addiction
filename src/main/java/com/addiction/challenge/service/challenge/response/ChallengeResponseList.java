package com.addiction.challenge.service.challenge.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChallengeResponseList {
    private Long challengeId;
    private String title;
    private String content;
    private String badge;
    private String finishYn;

    @Builder
    public ChallengeResponseList(Long challengeId, String title, String content, String badge, String finishYn) {
        this.challengeId = challengeId;
        this.title = title;
        this.content = content;
        this.badge = badge;
        this.finishYn = finishYn;
    }
}
