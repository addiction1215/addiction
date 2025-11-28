package com.addiction.mission.repository.Impl;

import com.addiction.mission.repository.MissionQueryRepository;
import com.addiction.mission.repository.MissionRepository;
import com.addiction.mission.service.mission.response.MissionResponseList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MissionRepositoryImpl implements MissionRepository {
    private final MissionQueryRepository missionQueryRepository;

    @Override
    public List<MissionResponseList> findByChallengeIdAndUserId(Long challengeId, long userId) {
        return missionQueryRepository.findByChallengeIdAndUserId(challengeId, userId);
    }
}
