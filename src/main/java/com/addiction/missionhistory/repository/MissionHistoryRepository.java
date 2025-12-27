package com.addiction.missionhistory.repository;

import com.addiction.missionhistory.entity.MissionHistory;

import java.util.List;

public interface MissionHistoryRepository {
    List<MissionHistory> findByChallengeHistoryId(Long challengeHistoryId);
}
