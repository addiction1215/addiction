package com.addiction.mission.repository;

import com.addiction.challenge.entity.Challenge;
import com.addiction.mission.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissionJpaRepository extends JpaRepository<Mission, Long> {
    /**
     * 특정 챌린지의 미션 목록 조회
     */
    List<Mission> findByChallengeId(Challenge challenge);
}
