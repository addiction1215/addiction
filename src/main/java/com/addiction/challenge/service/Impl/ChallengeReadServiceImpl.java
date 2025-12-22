package com.addiction.challenge.service.Impl;

import com.addiction.challenge.repository.ChallengeRepository;
import com.addiction.challenge.repository.response.ChallengeDto;
import com.addiction.challenge.service.ChallengeReadService;
import com.addiction.challenge.service.challenge.response.ChallengeListResponse;
import com.addiction.challenge.service.challenge.response.ChallengeResponse;
import com.addiction.common.enums.ChallengeStatus;
import com.addiction.global.page.request.PageInfoServiceRequest;
import com.addiction.global.security.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ChallengeReadServiceImpl implements ChallengeReadService {
    private final SecurityService securityService;
    private final ChallengeRepository challengeRepository;

    @Override
    public ChallengeListResponse getChallenge(PageInfoServiceRequest request) {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();
        List<ChallengeDto> challengePage = challengeRepository.findByUserId(userId);

        return ChallengeListResponse.createResponse(
                challengePage.stream()
                        .filter(challenge -> ChallengeStatus.PROGRESSING.toString().equalsIgnoreCase(challenge.getStatus().toString()))
                        .findFirst()
                        .map(ChallengeResponse::createResponse)
                        .orElse(null),
                challengePage.stream()
                        .filter(challenge -> ChallengeStatus.LEFT.toString().equalsIgnoreCase(challenge.getStatus().toString()))
                        .map(ChallengeResponse::createResponse)
                        .toList(),
                challengePage.stream()
                        .filter(challenge -> ChallengeStatus.COMPLETED.toString().equalsIgnoreCase(challenge.getStatus().toString()))
                        .map(ChallengeResponse::createResponse)
                        .toList()
        );
    }
}
