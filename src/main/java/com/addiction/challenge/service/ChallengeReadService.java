package com.addiction.challenge.service;

import com.addiction.challenge.service.challenge.response.ChallengeResponse;
import com.addiction.common.enums.YnStatus;
import com.addiction.global.page.request.PageInfoServiceRequest;
import com.addiction.global.page.response.PageCustom;

public interface ChallengeReadService {
    PageCustom<ChallengeResponse> getChallenge(YnStatus finishYn, PageInfoServiceRequest request);
}
