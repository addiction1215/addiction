package com.addiction.challenge.missionhistory.repository;

import com.addiction.challenge.missionhistory.entity.MissionHistory;
import com.addiction.challenge.missionhistory.entity.MissionStatus;
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

    @Query("SELECT COUNT(mh) FROM MissionHistory mh WHERE mh.challengeHistory.id = :challengeHistoryId")
    long countByChallengeHistoryId(@Param("challengeHistoryId") Long challengeHistoryId);

    @Query("SELECT COUNT(mh) FROM MissionHistory mh WHERE mh.challengeHistory.id = :challengeHistoryId AND mh.status = :status")
    long countByChallengeHistoryIdAndStatus(@Param("challengeHistoryId") Long challengeHistoryId,
                                            @Param("status") MissionStatus status);
}
