package com.addiction.mission.repository;

import com.addiction.mission.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MissionJpaRepository extends JpaRepository<Mission, Long> {
    
    /**
     * 챌린지 ID로 미션 목록 조회
     */
    @Query("SELECT m FROM Mission m WHERE m.challenge.id = :challengeId ORDER BY m.id ASC")
    List<Mission> findByChallengeId(@Param("challengeId") Long challengeId);
}
