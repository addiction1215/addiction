package com.addiction.challenge.mission.service;

import com.addiction.challenge.mission.service.response.MissionListResponse;

public interface MissionReadService {

    /**
     * 챌린지 ID로 미션 목록 조회
     * 사용자가 해당 챌린지에 참여했다면 각 미션의 완료 상태도 함께 반환
     */
    MissionListResponse getMissionListByChallengeId(Long challengeId);
}
