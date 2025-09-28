package com.addiction.challenge.service.Impl;

import com.addiction.challenge.entity.Challenge;
import com.addiction.challenge.repository.ChallengeRepository;
import com.addiction.challenge.service.ChallengeReadService;
import com.addiction.challenge.service.challenge.response.ChallengeResponse;
import com.addiction.challenge.service.challenge.response.ChallengeResponseList;
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
    public ChallengeResponse getChallenge(PageInfoServiceRequest request) {
        long userId = securityService.getCurrentLoginUserInfo().getUserId();

        List<ChallengeResponseList> challengePage = challengeRepository.findByUserId(
                userId
        );

        List<ChallengeResponseList> leftChallengeList = challengePage.stream()
                .filter(challenge -> YnStatus.N.toString().equalsIgnoreCase(challenge.getFinishYn()))
                .toList();

        List<ChallengeResponseList> finishedChallengeList = challengePage.stream()
                .filter(challenge -> YnStatus.Y.toString().equalsIgnoreCase(challenge.getFinishYn()))
                .toList();

        ChallengeResponse challengeResponse = ChallengeResponse.builder()
                .leftChallengeList(leftChallengeList)
                .finishedChallengeList(finishedChallengeList)
                .build();


        return challengeResponse;
    }
}
