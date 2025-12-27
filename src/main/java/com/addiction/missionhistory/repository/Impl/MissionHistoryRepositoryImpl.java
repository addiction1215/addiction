package com.addiction.missionhistory.repository.Impl;

import com.addiction.missionhistory.entity.MissionHistory;
import com.addiction.missionhistory.repository.MissionHistoryJpaRepository;
import com.addiction.missionhistory.repository.MissionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MissionHistoryRepositoryImpl implements MissionHistoryRepository {
    private final MissionHistoryJpaRepository missionHistoryJpaRepository;

    @Override
    public List<MissionHistory> findByChallengeHistoryId(Long challengeHistoryId) {
        return missionHistoryJpaRepository.findByChallengeHistoryId(challengeHistoryId);
    }
}
