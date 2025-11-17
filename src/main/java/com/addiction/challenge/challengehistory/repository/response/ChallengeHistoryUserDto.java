package com.addiction.challenge.challengehistory.repository.response;

import com.addiction.challenge.challenge.entity.Challenge;
import lombok.Getter;

@Getter
public class ChallengeHistoryUserDto {

    private final Long challengeId;
    private final String badge;
    private final String title;

    public ChallengeHistoryUserDto(Challenge challenge) {
        this.challengeId = challenge.getId();
        this.badge = challenge.getBadge();
        this.title = challenge.getTitle();
    }
}
