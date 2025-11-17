package com.addiction.challenge.challenge.service.challenge.response;

import com.addiction.challenge.challenge.entity.Challenge;
import com.addiction.common.enums.YnStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChallengeResponseList {
    private Long challengeId;
    private String title;
    private String content;
    private String badge;
    private YnStatus finishYn;

    @Builder
    public ChallengeResponseList(Long challengeId, String title, String content, String badge, YnStatus finishYn) {
        this.challengeId = challengeId;
        this.title = title;
        this.content = content;
        this.badge = badge;
        this.finishYn = finishYn;
    }

    public ChallengeResponseList(Challenge challenge, YnStatus finishYn) {
        this.challengeId = challenge.getId();
        this.title = challenge.getTitle();
        this.content = challenge.getContent();
        this.badge = challenge.getBadge();
        this.finishYn = finishYn;
    }
}
