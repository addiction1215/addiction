package com.addiction.challenge.missionhistory.service.impl;

import com.addiction.challenge.challengehistory.entity.ChallengeHistory;
import com.addiction.challenge.challengehistory.repository.ChallengeHistoryJpaRepository;
import com.addiction.challenge.missionhistory.entity.MissionHistory;
import com.addiction.challenge.missionhistory.entity.MissionStatus;
import com.addiction.challenge.missionhistory.repository.MissionHistoryRepository;
import com.addiction.challenge.missionhistory.service.MissionHistoryReadService;
import com.addiction.challenge.missionhistory.service.response.MissionHistoryResponse;
import com.addiction.challenge.missionhistory.service.response.MissionProgressResponse;
import com.addiction.global.exception.AddictionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MissionHistoryReadServiceImpl implements MissionHistoryReadService {
    private final MissionHistoryRepository missionHistoryRepository;
    private final ChallengeHistoryJpaRepository challengeHistoryJpaRepository;

    @Override
    public MissionProgressResponse getMissionProgress(Long challengeHistoryId) {
        // ChallengeHistory 조회
        ChallengeHistory challengeHistory = challengeHistoryJpaRepository.findById(challengeHistoryId)
                .orElseThrow(() -> new AddictionException("챌린지 이력을 찾을 수 없습니다."));

        // 해당 챌린지의 모든 미션 조회
        List<MissionHistory> missionHistories = missionHistoryRepository.findByChallengeHistoryId(challengeHistory.getId());

        // 총 완료된 미션 수
        int completedCount = (int) missionHistories.stream()
                .filter(mh -> mh.getStatus() == MissionStatus.COMPLETED)
                .count();
        // 총 획득 포인트 계산
        int totalEarnedReward = missionHistories.stream()
                .filter(mh -> mh.getStatus() == MissionStatus.COMPLETED)
                .mapToInt(mh -> mh.getMission().getReward())
                .sum();

        // 총 가능 포인트 계산
        int totalPossibleReward = missionHistories.stream()
                .mapToInt(value -> value.getMission().getReward())
                .sum();

        return MissionProgressResponse.createResponse(
                challengeHistory.getId(),
                challengeHistory.getChallenge().getId(),
                challengeHistory.getChallenge().getTitle(),
                missionHistories.size(),
                completedCount,
                totalPossibleReward,
                totalEarnedReward,
                missionHistories.stream().map(MissionHistoryResponse::createResponse).toList()
        );
    }

    @Override
    public MissionHistory findById(Long missionHistoryId) {
        return missionHistoryRepository.findById(missionHistoryId)
                .orElseThrow(() -> new AddictionException("미션 이력을 찾을 수 없습니다."));
    }
}
