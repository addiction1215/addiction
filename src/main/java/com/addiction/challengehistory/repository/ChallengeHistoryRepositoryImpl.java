package com.addiction.challengehistory.repository;

import com.addiction.challenge.entity.Challenge;
import com.addiction.challengehistory.entity.ChallengeHistory;
import com.addiction.common.enums.ChallengeStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ChallengeHistoryRepositoryImpl implements ChallengeHistoryRepository {
    
    private final ChallengeHistoryJpaRepository challengeHistoryJpaRepository;
    private final ChallengeHistoryQueryRepository challengeHistoryQueryRepository;

    @Override
    public Optional<ChallengeHistory> findProgressingChallenge(Long userId) {
        return challengeHistoryJpaRepository.findFirstByUserIdAndStatus(userId, ChallengeStatus.PROGRESSING);
    }

    @Override
    public Page<ChallengeHistory> findCompletedChallenges(Long userId, Pageable pageable) {
        return challengeHistoryJpaRepository.findByUserIdAndStatus(userId, ChallengeStatus.COMPLETED, pageable);
    }

    @Override
    public Page<Challenge> findLeftChallenges(Long userId, Pageable pageable) {
        return challengeHistoryQueryRepository.findLeftChallenges(userId, pageable);
    }

    @Override
    public ChallengeHistory save(ChallengeHistory challengeHistory) {
        return challengeHistoryJpaRepository.save(challengeHistory);
    }

    @Override
    public Optional<ChallengeHistory> findByUserIdAndChallengeId(Long userId, Long challengeId) {
        return challengeHistoryJpaRepository.findByUserIdAndChallengeId(userId, challengeId);
    }
}
