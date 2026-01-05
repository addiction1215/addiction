package com.addiction.challenge.challengehistory.service;

import com.addiction.challenge.challange.entity.Challenge;
import com.addiction.challenge.challengehistory.controller.request.ChallengeCancelRequest;
import com.addiction.challenge.challengehistory.controller.request.ChallengeJoinRequest;
import com.addiction.challenge.challengehistory.controller.request.FailChallengeHistoryRequest;
import com.addiction.challenge.challengehistory.service.response.ChallengeCancelResponse;
import com.addiction.challenge.challengehistory.service.response.ChallengeJoinResponse;

public interface ChallengeHistoryService {
    /**
     * 챌린지 참여하기
     */
    ChallengeJoinResponse joinChallenge(ChallengeJoinRequest request);

    /**
     * 챌린지 포기하기
     */
    ChallengeCancelResponse cancelChallenge(ChallengeCancelRequest request);
}
