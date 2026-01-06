package com.addiction.challenge.challange.service.Impl;

import com.addiction.challenge.challange.entity.Challenge;
import com.addiction.challenge.challange.repository.ChallengeRepository;
import com.addiction.challenge.challange.service.ChallengeReadService;
import com.addiction.challenge.challange.service.response.ChallengeResponse;
import com.addiction.challenge.challengehistory.repository.ChallengeHistoryRepository;
import com.addiction.global.exception.AddictionException;
import com.addiction.global.page.request.PageInfoServiceRequest;
import com.addiction.global.page.response.PageCustom;
import com.addiction.global.page.response.PageableCustom;
import com.addiction.global.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChallengeReadServiceImpl implements ChallengeReadService {
    private final SecurityService securityService;
    private final ChallengeHistoryRepository challengeHistoryRepository;

    private final ChallengeRepository challengeRepository;


    @Override
    public PageCustom<ChallengeResponse> getLeftChallengeList(PageInfoServiceRequest request) {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();
        Page<Challenge> page = challengeHistoryRepository.findLeftChallenges(userId, request.toPageable());

        List<ChallengeResponse> challengeResponses = page.getContent().stream()
                .map(ChallengeResponse::createResponse)
                .toList();

        return PageCustom.<ChallengeResponse>builder()
                .content(challengeResponses)
                .pageInfo(PageableCustom.of(page))
                .build();
    }

    @Override
    public Challenge findById(Long challengeId) {
        return challengeRepository.findById(challengeId)
                .orElseThrow(() -> new AddictionException("존재하지 않는 챌린지입니다."));
    }
}
