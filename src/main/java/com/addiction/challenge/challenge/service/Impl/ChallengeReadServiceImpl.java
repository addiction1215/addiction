package com.addiction.challenge.challenge.service.Impl;

import com.addiction.challenge.challenge.repository.ChallengeRepository;
import com.addiction.challenge.challenge.service.ChallengeReadService;
import com.addiction.challenge.challenge.service.challenge.response.ChallengeResponse;
import com.addiction.challenge.challenge.service.challenge.response.ChallengeResponseList;
import com.addiction.challenge.challenge.service.response.ChallengeDetailResponse;
import com.addiction.common.enums.YnStatus;
import com.addiction.global.page.request.PageInfoServiceRequest;
import com.addiction.global.security.SecurityService;
import lombok.RequiredArgsConstructor;
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
    public ChallengeResponse getChallenge(PageInfoServiceRequest request) {
        long userId = securityService.getCurrentLoginUserInfo().getUserId();

        List<ChallengeResponseList> challengePage = challengeRepository.findByUserId(
                userId
        );

        List<ChallengeResponseList> leftChallengeList = challengePage.stream()
                .filter(challenge -> YnStatus.N.equals(challenge.getFinishYn()))
                .toList();

        List<ChallengeResponseList> finishedChallengeList = challengePage.stream()
                .filter(challenge -> YnStatus.Y.equals(challenge.getFinishYn()))
                .toList();

        return ChallengeResponse.builder()
                .leftChallengeList(leftChallengeList)
                .finishedChallengeList(finishedChallengeList)
                .build();
    }

    @Override
    public ChallengeDetailResponse findById(Long challengeId) {
        return ChallengeDetailResponse.createResponse(challengeRepository.findById(challengeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 챌린지는 없습니다. id = " + challengeId)));
    }
}
