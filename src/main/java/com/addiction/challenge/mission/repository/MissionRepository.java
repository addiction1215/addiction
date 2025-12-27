package com.addiction.challenge.mission.repository;

import com.addiction.challenge.mission.entity.Mission;

import java.util.List;

public interface MissionRepository {
    
    /**
     * 챌린지 ID로 미션 목록 조회
     */
    List<Mission> findByChallengeId(Long challengeId);
}
