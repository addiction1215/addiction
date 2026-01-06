package com.addiction.challenge.challengehistory.repository;

import com.addiction.challenge.challengehistory.entity.ChallengeHistory;
import com.addiction.challenge.challengehistory.entity.ChallengeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChallengeHistoryJpaRepository extends JpaRepository<ChallengeHistory, Long> {

    /**
     * 사용자의 특정 챌린지 히스토리 조회
     */
    @Query("SELECT ch FROM ChallengeHistory ch " +
            "WHERE ch.user.id = :userId AND ch.challenge.id = :challengeId")
    Optional<ChallengeHistory> findByUserIdAndChallengeId(@Param("userId") Long userId,
                                                          @Param("challengeId") Long challengeId);

    /**
     * 사용자의 진행중인 챌린지 조회 (최신순 1개)
     */
    @Query("SELECT ch FROM ChallengeHistory ch " +
            "WHERE ch.user.id = :userId AND ch.status = :status " +
            "ORDER BY ch.createdDate DESC")
    Optional<ChallengeHistory> findFirstByUserIdAndStatus(@Param("userId") Long userId,
                                                          @Param("status") ChallengeStatus status);

    /**
     * 사용자의 특정 상태 챌린지 목록 조회 (페이징)
     */
    @Query("SELECT ch FROM ChallengeHistory ch " +
            "WHERE ch.user.id = :userId AND ch.status = :status " +
            "ORDER BY ch.createdDate DESC")
    Page<ChallengeHistory> findByUserIdAndStatus(@Param("userId") Long userId,
                                                 @Param("status") ChallengeStatus status,
                                                 Pageable pageable);
}
