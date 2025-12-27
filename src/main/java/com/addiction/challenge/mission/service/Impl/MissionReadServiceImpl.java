package com.addiction.challenge.mission.service.Impl;

import com.addiction.challenge.challange.entity.Challenge;
import com.addiction.challenge.challange.repository.ChallengeJpaRepository;
import com.addiction.global.exception.AddictionException;
import com.addiction.challenge.mission.repository.MissionRepository;
import com.addiction.challenge.mission.service.MissionReadService;
import com.addiction.challenge.mission.service.response.MissionListResponse;
import com.addiction.challenge.mission.service.response.MissionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MissionReadServiceImpl implements MissionReadService {

    private final MissionRepository missionRepository;
    private final ChallengeJpaRepository challengeJpaRepository;

    @Override
    public MissionListResponse getMissionListByChallengeId(Long challengeId) {
        // 챌린지 정보 조회
        Challenge challenge = challengeJpaRepository.findById(challengeId)
                .orElseThrow(() -> new AddictionException("챌린지를 찾을 수 없습니다."));

        // 챌린지에 속한 미션 목록 조회
        List<MissionResponse> missions = missionRepository.findByChallengeId(challengeId).stream()
                .map(MissionResponse::createResponse)
                .toList();

        // 전체 보상 포인트 계산
        Integer totalReward = missions.stream()
                .mapToInt(MissionResponse::getReward)
                .sum();

        return MissionListResponse.createResponse(
                challenge,
                missions,
                totalReward
        );
    }
}
