package com.addiction.challengehistory.service.Impl;

import com.addiction.challengehistory.controller.request.FailChallengeHistoryRequest;
import com.addiction.challengehistory.entity.ChallengeHistory;
import com.addiction.challengehistory.repository.ChallengeHistoryRepository;
import com.addiction.challengehistory.service.ChallengeHistoryService;
import com.addiction.common.enums.ChallengeStatus;
import com.addiction.global.exception.AddictionException;
import com.addiction.global.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChallengeHistoryServiceImpl implements ChallengeHistoryService {
    private final SecurityService securityService;
    private final ChallengeHistoryRepository challengeHistoryRepository;

    @Override
    public void updateFailChallenge(FailChallengeHistoryRequest request) {
        long userId = securityService.getCurrentLoginUserInfo().getUserId();

        // 사용자의 해당 챌린지 히스토리 조회
        ChallengeHistory challengeHistory = challengeHistoryRepository
                .findByUserIdAndChallengeId(userId, request.getChallengeId())
                .orElseThrow(() -> new AddictionException("챌린지를 찾을 수 없습니다."));

        // 상태를 CANCELLED로 변경
        challengeHistory.updateStatus(ChallengeStatus.CANCELLED);
    }
}
