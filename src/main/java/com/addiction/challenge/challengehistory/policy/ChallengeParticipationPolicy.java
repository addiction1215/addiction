package com.addiction.challenge.challengehistory.policy;

import com.addiction.challenge.challengehistory.entity.ChallengeStatus;

import java.util.Set;

public final class ChallengeParticipationPolicy {

    public static final Set<ChallengeStatus> REJOIN_BLOCKING_STATUSES = Set.of(
            ChallengeStatus.PROGRESSING,
            ChallengeStatus.COMPLETED
    );

    private ChallengeParticipationPolicy() {
    }
}
