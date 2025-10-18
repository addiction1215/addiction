package com.addiction.challenge.service.challenge.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProgressingChallenge {
    private Long challengeId;
    private String title;
    private String content;
    private Integer progressPercent;

    @Builder
    public ProgressingChallenge(Long challengeId, String title, String content, Integer progressPercent) {
        this.challengeId = challengeId;
        this.title = title;
        this.content = content;
        this.progressPercent = progressPercent;
    }
}
