package com.addiction.challenge.missionhistory.repository.Impl;

import com.addiction.challenge.missionhistory.entity.MissionHistory;
import com.addiction.challenge.missionhistory.repository.MissionHistoryJpaRepository;
import com.addiction.challenge.missionhistory.repository.MissionHistoryRepository;
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

    @Override
    public List<MissionHistory> saveAll(List<MissionHistory> missionHistories) {
        return missionHistoryJpaRepository.saveAll(missionHistories);
    }
}
