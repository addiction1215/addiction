package com.addiction.challenge.service.Impl;

import com.addiction.alertHistory.entity.AlertHistory;
import com.addiction.challenge.entity.Challenge;
import com.addiction.challenge.repository.ChallengeRepository;
import com.addiction.challenge.service.ChallengeReadService;
import com.addiction.challenge.service.challenge.response.ChallengeResponse;
import com.addiction.common.enums.YnStatus;
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
    private final ChallengeRepository challengeRepository;

    @Override
    public PageCustom<ChallengeResponse> getChallenge(YnStatus finishYn, PageInfoServiceRequest request) {
        long userId = securityService.getCurrentLoginUserInfo().getUserId();

        Page<Challenge> challengePage = challengeRepository.findByFinishYnAndUserId(
                finishYn,
                userId,
                request.toPageable()
        );

        List<ChallengeResponse> challengeResponseList = challengePage.getContent().stream()
                .map(ChallengeResponse::of)
                .toList();

        return PageCustom.<ChallengeResponse>builder()
                .content(challengeResponseList)
                .pageInfo(PageableCustom.of(challengePage))
                .build();
    }
}
