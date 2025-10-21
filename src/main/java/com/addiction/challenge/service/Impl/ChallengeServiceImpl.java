package com.addiction.challenge.service.Impl;

import com.addiction.challenge.service.ChallengeService;
import com.addiction.challenge.service.challenge.request.FailChallengeRequest;
import com.addiction.global.security.SecurityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ChallengeServiceImpl implements ChallengeService {
    private final SecurityService securityService;

    @Override
    public void insertFailChallenge(FailChallengeRequest request) {
        long userId = securityService.getCurrentLoginUserInfo().getUserId();

    }
}
