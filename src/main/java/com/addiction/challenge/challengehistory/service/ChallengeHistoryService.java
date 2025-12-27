package com.addiction.challenge.challengehistory.service;

import com.addiction.challenge.challengehistory.controller.request.FailChallengeHistoryRequest;

public interface ChallengeHistoryService {
    void updateFailChallenge(FailChallengeHistoryRequest request);
}
