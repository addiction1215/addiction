package com.addiction.challenge.service;

import com.addiction.challenge.service.challenge.response.ChallengeListResponse;
import com.addiction.challenge.service.challenge.response.ChallengeResponse;
import com.addiction.global.page.request.PageInfoServiceRequest;
import com.addiction.global.page.response.PageCustom;

public interface ChallengeReadService {
    ChallengeListResponse getChallenge(PageInfoServiceRequest request);
}
