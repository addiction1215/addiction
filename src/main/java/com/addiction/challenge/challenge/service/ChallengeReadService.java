package com.addiction.challenge.challenge.service;

import com.addiction.challenge.challenge.service.challenge.response.ChallengeResponse;
import com.addiction.challenge.challenge.service.response.ChallengeDetailResponse;
import com.addiction.global.page.request.PageInfoServiceRequest;

public interface ChallengeReadService {
    ChallengeResponse getChallenge(PageInfoServiceRequest request);

    ChallengeDetailResponse findById(Long challengeId);
}
