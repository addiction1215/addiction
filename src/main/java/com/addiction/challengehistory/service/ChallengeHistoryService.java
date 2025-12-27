package com.addiction.challengehistory.service;

import com.addiction.challengehistory.controller.request.FailChallengeHistoryRequest;

public interface ChallengeHistoryService {
    void updateFailChallenge(FailChallengeHistoryRequest request);
}
