package com.addiction.challenge.missionhistory.repository;

import com.addiction.challenge.missionhistory.entity.MissionHistory;

import java.util.List;

public interface MissionHistoryRepository {
    List<MissionHistory> findByChallengeHistoryId(Long challengeHistoryId);
}
