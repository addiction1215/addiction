package com.addiction.challenge.service.challenge.response;

import com.addiction.common.enums.ChallengeStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChallengeResponseList {
    private Long challengeId;
    private String title;
    private String content;
    private String badge;
    private ChallengeStatus status;

    @Builder
    public ChallengeResponseList(Long challengeId, String title, String content, String badge, ChallengeStatus status) {
        this.challengeId = challengeId;
        this.title = title;
        this.content = content;
        this.badge = badge;
        this.status = status;
    }
}
