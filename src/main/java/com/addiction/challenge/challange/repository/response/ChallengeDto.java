package com.addiction.challenge.challange.repository.response;

import com.addiction.common.enums.ChallengeStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChallengeDto {
    private final Long challengeId;
    private final String title;
    private final String content;
    private final String badge;
    private final ChallengeStatus status;

    @Builder
    public ChallengeDto(Long challengeId, String title, String content, String badge, ChallengeStatus status) {
        this.challengeId = challengeId;
        this.title = title;
        this.content = content;
        this.badge = badge;
        this.status = status;
    }
}
