package com.addiction.challenge.missionhistory.repository;

import com.addiction.challenge.missionhistory.entity.MissionHistory;
import com.addiction.challenge.missionhistory.entity.MissionStatus;

import java.util.List;
import java.util.Optional;

public interface MissionHistoryRepository {
    List<MissionHistory> findByChallengeHistoryId(Long challengeHistoryId);

    List<MissionHistory> saveAll(List<MissionHistory> missionHistories);

    Optional<MissionHistory> findById(Long id);

    long countByChallengeHistoryId(Long challengeHistoryId);

    long countByChallengeHistoryIdAndStatus(Long challengeHistoryId, MissionStatus status);
}
