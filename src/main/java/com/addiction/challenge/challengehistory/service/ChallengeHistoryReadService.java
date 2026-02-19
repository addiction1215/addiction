package com.addiction.challenge.challengehistory.service;

import com.addiction.challenge.challengehistory.entity.ChallengeHistory;
import com.addiction.challenge.challengehistory.service.response.ChallengeHistoryResponse;
import com.addiction.challenge.challengehistory.service.response.FinishedChallengeHistoryResponse;
import com.addiction.global.page.request.PageInfoServiceRequest;
import com.addiction.global.page.response.PageCustom;

public interface ChallengeHistoryReadService {
    // 진행중인 챌린지 1개 조회
    ChallengeHistoryResponse getProgressingChallenge();

    // 완료된 챌린지 목록 조회 (페이징)
    PageCustom<FinishedChallengeHistoryResponse> getFinishedChallengeList(PageInfoServiceRequest request);

    ChallengeHistory findById(Long challengeHistoryId);
}
