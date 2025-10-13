package com.addiction.challenge.challengehistory.service;

import com.addiction.challenge.challengehistory.service.response.ChallengeHistoryUserResponse;

import java.util.List;

public interface ChallengeHistoryReadService {

    List<ChallengeHistoryUserResponse> findByUserId();

}
