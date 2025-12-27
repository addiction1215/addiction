package com.addiction.challenge.service;

import com.addiction.challenge.service.challenge.response.ChallengeResponse;
import com.addiction.global.page.request.PageInfoServiceRequest;
import com.addiction.global.page.response.PageCustom;

public interface ChallengeReadService {
    // 진행중인 챌린지 1개 조회
    ChallengeResponse getProgressingChallenge();

    // 남은 챌린지 목록 조회 (페이징)
    PageCustom<ChallengeResponse> getLeftChallengeList(PageInfoServiceRequest request);

    // 완료된 챌린지 목록 조회 (페이징)
    PageCustom<ChallengeResponse> getFinishedChallengeList(PageInfoServiceRequest request);
}
