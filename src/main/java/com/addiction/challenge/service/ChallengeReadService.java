package com.addiction.challenge.service;

import com.addiction.challenge.service.response.ChallengeResponse;
import com.addiction.global.page.request.PageInfoServiceRequest;
import com.addiction.global.page.response.PageCustom;

public interface ChallengeReadService {

    // 남은 챌린지 목록 조회 (페이징)
    PageCustom<ChallengeResponse> getLeftChallengeList(PageInfoServiceRequest request);

}
