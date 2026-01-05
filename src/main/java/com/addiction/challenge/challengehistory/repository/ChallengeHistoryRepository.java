package com.addiction.challenge.challengehistory.repository;

import com.addiction.challenge.challange.entity.Challenge;
import com.addiction.challenge.challengehistory.entity.ChallengeHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ChallengeHistoryRepository {
    
    /**
     * 사용자의 진행중인 챌린지 조회
     */
    Optional<ChallengeHistory> findProgressingChallenge(Long userId);
    
    /**
     * 사용자의 완료된 챌린지 목록 조회
     */
    Page<ChallengeHistory> findCompletedChallenges(Long userId, Pageable pageable);
    
    /**
     * 남은 챌린지 목록 조회 (완료하지 않은 챌린지)
     */
    Page<Challenge> findLeftChallenges(Long userId, Pageable pageable);
    
    /**
     * 챌린지 히스토리 저장
     */
    ChallengeHistory save(ChallengeHistory challengeHistory);
    
    /**
     * 사용자의 특정 챌린지 히스토리 조회
     */
    Optional<ChallengeHistory> findByUserIdAndChallengeId(Long userId, Long challengeId);

    Optional<ChallengeHistory> findById(Long challengeHistoryId);
}
