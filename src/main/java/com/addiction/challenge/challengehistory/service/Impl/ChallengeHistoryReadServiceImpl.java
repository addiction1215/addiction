package com.addiction.challenge.challengehistory.service.Impl;

import com.addiction.challenge.challengehistory.entity.ChallengeHistory;
import com.addiction.challenge.challengehistory.repository.ChallengeHistoryRepository;
import com.addiction.challenge.challengehistory.service.ChallengeHistoryReadService;
import com.addiction.challenge.challengehistory.service.response.ChallengeHistoryResponse;
import com.addiction.challenge.challengehistory.service.response.FinishedChallengeHistoryResponse;
import com.addiction.challenge.missionhistory.entity.MissionStatus;
import com.addiction.challenge.missionhistory.repository.MissionHistoryRepository;
import com.addiction.global.exception.AddictionException;
import com.addiction.global.page.request.PageInfoServiceRequest;
import com.addiction.global.page.response.PageCustom;
import com.addiction.global.page.response.PageableCustom;
import com.addiction.global.security.SecurityService;
import com.addiction.storage.enums.BucketKind;
import com.addiction.storage.service.S3StorageService;
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
    private final S3StorageService s3StorageService;

    private final ChallengeHistoryRepository challengeHistoryRepository;
    private final MissionHistoryRepository missionHistoryRepository;

    @Override
    public ChallengeHistoryResponse getProgressingChallenge() {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();

        return challengeHistoryRepository.findProgressingChallenge(userId)
                .map(challengeHistory -> {
                    String badge = s3StorageService.createPresignedUrl(challengeHistory.getChallenge().getBadge(), BucketKind.CHALLENGE_BADGE);
                    Integer progress = calculateProgress(challengeHistory.getId());
                    return ChallengeHistoryResponse.createResponse(challengeHistory, badge, progress);
                })
                .orElse(null);
    }

    private Integer calculateProgress(Long challengeHistoryId) {
        long total = missionHistoryRepository.countByChallengeHistoryId(challengeHistoryId);
        if (total == 0) {
            return 0;
        }
        long completed = missionHistoryRepository.countByChallengeHistoryIdAndStatus(challengeHistoryId, MissionStatus.COMPLETED);
        return (int) (completed * 100 / total);
    }

    @Override
    public PageCustom<FinishedChallengeHistoryResponse> getFinishedChallengeList(PageInfoServiceRequest request) {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();
        PageRequest pageRequest = PageRequest.of(request.getPage() - 1, request.getSize());

        Page<ChallengeHistory> page = challengeHistoryRepository.findCompletedChallenges(userId, pageRequest);

        List<FinishedChallengeHistoryResponse> challengeResponses = page.getContent().stream()
                .map(challengeHistory -> FinishedChallengeHistoryResponse.createResponse(challengeHistory, s3StorageService.createPresignedUrl(challengeHistory.getChallenge().getBadge(), BucketKind.CHALLENGE_BADGE)))
                .toList();

        return PageCustom.<FinishedChallengeHistoryResponse>builder()
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
