package com.addiction.challenge.service.Impl;

import com.addiction.challenge.repository.ChallengeRepository;
import com.addiction.challenge.repository.response.ChallengeDto;
import com.addiction.challenge.service.ChallengeReadService;
import com.addiction.challenge.service.challenge.response.ChallengeResponse;
import com.addiction.common.enums.ChallengeStatus;
import com.addiction.global.page.request.PageInfoServiceRequest;
import com.addiction.global.page.response.PageCustom;
import com.addiction.global.page.response.PageableCustom;
import com.addiction.global.security.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public ChallengeResponse getProgressingChallenge() {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();

        return challengeRepository.findProgressingChallengeByUserId(userId)
                .map(ChallengeResponse::createResponse)
                .orElse(null);
    }

    @Override
    public PageCustom<ChallengeResponse> getLeftChallengeList(PageInfoServiceRequest request) {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();
        PageRequest pageRequest = PageRequest.of(request.getPage(), request.getSize());

        Page<ChallengeDto> page = challengeRepository.findByUserIdAndStatus(userId, ChallengeStatus.LEFT, pageRequest);

        List<ChallengeResponse> challengeResponses = page.getContent().stream()
                .map(ChallengeResponse::createResponse)
                .toList();

        return PageCustom.<ChallengeResponse>builder()
                .content(challengeResponses)
                .pageInfo(PageableCustom.of(page))
                .build();
    }

    @Override
    public PageCustom<ChallengeResponse> getFinishedChallengeList(PageInfoServiceRequest request) {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();
        PageRequest pageRequest = PageRequest.of(request.getPage(), request.getSize());

        Page<ChallengeDto> page = challengeRepository.findByUserIdAndStatus(userId, ChallengeStatus.COMPLETED, pageRequest);

        List<ChallengeResponse> challengeResponses = page.getContent().stream()
                .map(ChallengeResponse::createResponse)
                .toList();

        return PageCustom.<ChallengeResponse>builder()
                .content(challengeResponses)
                .pageInfo(PageableCustom.of(page))
                .build();
    }
}
