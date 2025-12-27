package com.addiction.missionhistory.repository;

import com.addiction.common.enums.MissionStatus;
import com.addiction.missionhistory.entity.MissionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MissionHistoryJpaRepository extends JpaRepository<MissionHistory, Long> {
    @Query("SELECT mh FROM MissionHistory mh " +
            "WHERE mh.challengeHistory.id = :challengeHistoryId " +
            "AND mh.status = :status")
    List<MissionHistory> findByChallengeHistoryIdAndStatus(
            @Param("challengeHistoryId") Long challengeHistoryId,
            @Param("status") MissionStatus status
    );

    @Query("SELECT mh FROM MissionHistory mh WHERE mh.challengeHistory.id = :challengeHistoryId")
    List<MissionHistory> findByChallengeHistoryId(@Param("challengeHistoryId") Long challengeHistoryId);
}
