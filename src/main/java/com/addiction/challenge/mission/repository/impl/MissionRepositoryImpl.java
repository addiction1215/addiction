package com.addiction.challenge.mission.repository.impl;

import com.addiction.challenge.mission.entity.Mission;
import com.addiction.challenge.mission.repository.MissionJpaRepository;
import com.addiction.challenge.mission.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class MissionRepositoryImpl implements MissionRepository {
    
    private final MissionJpaRepository missionJpaRepository;

    @Override
    public List<Mission> findByChallengeId(Long challengeId) {
        return missionJpaRepository.findByChallengeId(challengeId);
    }
}
