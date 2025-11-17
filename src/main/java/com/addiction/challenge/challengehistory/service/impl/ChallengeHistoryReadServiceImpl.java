package com.addiction.challenge.challengehistory.service.impl;

import com.addiction.challenge.challengehistory.repository.ChallengeHistoryRepository;
import com.addiction.challenge.challengehistory.service.ChallengeHistoryReadService;
import com.addiction.challenge.challengehistory.service.response.ChallengeHistoryUserResponse;
import com.addiction.global.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChallengeHistoryReadServiceImpl implements ChallengeHistoryReadService {

    private final SecurityService securityService;

    private final ChallengeHistoryRepository challengeHistoryRepository;

    @Override
    public List<ChallengeHistoryUserResponse> findByUserId() {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();
        return challengeHistoryRepository.findByUserId(userId).stream()
                .map(ChallengeHistoryUserResponse::createResponse).toList();
    }
}
