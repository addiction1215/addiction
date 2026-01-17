package com.addiction.challenge.challengehistory.service.Impl;

import com.addiction.challenge.challengehistory.entity.ChallengeHistory;
import com.addiction.challenge.challengehistory.repository.ChallengeHistoryRepository;
import com.addiction.challenge.challengehistory.service.ChallengeHistoryReadService;
import com.addiction.challenge.challengehistory.service.response.ChallengeHistoryResponse;
import com.addiction.global.exception.AddictionException;
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
public class ChallengeHistoryReadServiceImpl implements ChallengeHistoryReadService {
    private final SecurityService securityService;
    private final ChallengeHistoryRepository challengeHistoryRepository;

    @Override
    public ChallengeHistoryResponse getProgressingChallenge() {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();

        return challengeHistoryRepository.findProgressingChallenge(userId)
                .map(ChallengeHistoryResponse::createResponse)
                .orElse(null);
    }


    @Override
    public PageCustom<ChallengeHistoryResponse> getFinishedChallengeList(PageInfoServiceRequest request) {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();
        PageRequest pageRequest = PageRequest.of(request.getPage() - 1, request.getSize());

        Page<ChallengeHistory> page = challengeHistoryRepository.findCompletedChallenges(userId, pageRequest);

        List<ChallengeHistoryResponse> challengeResponses = page.getContent().stream()
                .map(ChallengeHistoryResponse::createResponse)
                .toList();

        return PageCustom.<ChallengeHistoryResponse>builder()
                .content(challengeResponses)
                .pageInfo(PageableCustom.of(page))
                .build();
    }

    @Override
    public ChallengeHistory findById(Long challengeHistoryId) {
        return challengeHistoryRepository
                .findById(challengeHistoryId)
                .orElseThrow(() -> new AddictionException("존재하지 않는 챌린지 이력입니다."));
    }

}
