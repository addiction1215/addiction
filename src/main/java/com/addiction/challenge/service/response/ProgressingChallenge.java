package com.addiction.challenge.service.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProgressingChallenge {
    private final Long challengeId;
    private final String title;
    private final String content;
    private final Integer progressPercent;

    @Builder
    public ProgressingChallenge(Long challengeId, String title, String content, Integer progressPercent) {
        this.challengeId = challengeId;
        this.title = title;
        this.content = content;
        this.progressPercent = progressPercent;
    }

    public static ProgressingChallenge createProgressingChallenge(Long challengeId, String title, String content, Integer progressPercent) {
        return ProgressingChallenge.builder()
                .challengeId(challengeId)
                .title(title)
                .content(content)
                .progressPercent(progressPercent)
                .build();
    }
}
