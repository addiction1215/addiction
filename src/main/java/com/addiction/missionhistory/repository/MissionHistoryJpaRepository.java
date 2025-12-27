package com.addiction.missionhistory.repository;

import com.addiction.missionhistory.entity.MissionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MissionHistoryJpaRepository extends JpaRepository<MissionHistory, Long> {
    @Query("SELECT mh FROM MissionHistory mh WHERE mh.user.id = :userId AND mh.mission.challenge.id = :challengeId")
    List<MissionHistory> findByUserIdAndChallengeId(@Param("userId") Long userId, @Param("challengeId") Long challengeId);
}
