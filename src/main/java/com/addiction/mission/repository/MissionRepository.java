package com.addiction.mission.repository;

import com.addiction.mission.service.mission.response.MissionResponseList;

import java.util.List;

public interface MissionRepository {
    List<MissionResponseList> findByChallengeIdAndUserId(Long challengeId, long userId);
}
