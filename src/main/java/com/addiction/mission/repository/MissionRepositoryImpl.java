package com.addiction.mission.repository;

import com.addiction.mission.entity.Mission;
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
