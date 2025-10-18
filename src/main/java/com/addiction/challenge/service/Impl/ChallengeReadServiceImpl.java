package com.addiction.challenge.service.Impl;

import com.addiction.challenge.repository.ChallengeRepository;
import com.addiction.challenge.service.ChallengeReadService;
import com.addiction.challenge.service.challenge.response.ChallengeResponse;
import com.addiction.challenge.service.challenge.response.ChallengeResponseList;
import com.addiction.challenge.service.challenge.response.ProgressingChallenge;
import com.addiction.common.enums.ChallengeStatus;
import com.addiction.common.enums.YnStatus;
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
    public ChallengeResponse getChallenge(PageInfoServiceRequest request) {
        long userId = securityService.getCurrentLoginUserInfo().getUserId();

        List<ChallengeResponseList> challengePage = challengeRepository.findByUserId(
                userId
        );

        ChallengeResponseList progressingChallenge = challengePage.stream()
                .filter(challenge -> ChallengeStatus.PROGRESSING.toString().equalsIgnoreCase(challenge.getStatus().toString()))
                .findFirst() // Stream에서 첫 번째 항목을 찾음 (Optional<ChallengeResponseList> 반환)
                .orElse(null);

        List<ChallengeResponseList> leftChallengeList = challengePage.stream()
                .filter(challenge -> ChallengeStatus.LEFT.toString().equalsIgnoreCase(challenge.getStatus().toString()))
                .toList();

        List<ChallengeResponseList> finishedChallengeList = challengePage.stream()
                .filter(challenge -> ChallengeStatus.COMPLETED.toString().equalsIgnoreCase(challenge.getStatus().toString()))
                .toList();

        ProgressingChallenge finalProgressingChallenge = (progressingChallenge != null)
                ? ProgressingChallenge.builder()
                .challengeId(progressingChallenge.getChallengeId()) // ChallengeResponseList의 필드 사용
                .title(progressingChallenge.getTitle())
                .content(progressingChallenge.getContent())
//                 .progressPercent(???) // 🚨 'progressPercent'는 DTO에 없으므로 값을 정해야 합니다.
                .progressPercent(0) // 예: 기본값 0 또는 별도 계산
                .build()
                : null;

        ChallengeResponse challengeResponse = ChallengeResponse.builder()
                .progressingChallenge(finalProgressingChallenge)
                .leftChallengeList(leftChallengeList)
                .finishedChallengeList(finishedChallengeList)
                .build();


        return challengeResponse;
    }
}
