package com.addiction.challenge.repository.Impl;

import com.addiction.challenge.entity.Challenge;
import com.addiction.challenge.repository.ChallengeJpaRepository;
import com.addiction.challenge.repository.ChallengeQueryRepository;
import com.addiction.challenge.repository.ChallengeRepository;
import com.addiction.challenge.repository.response.ChallengeDto;
import com.addiction.common.enums.ChallengeStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChallengeRepositoryImpl implements ChallengeRepository {
    private final ChallengeQueryRepository challengeQueryRepository;
    private final ChallengeJpaRepository challengeJpaRepository;

    @Override
    public List<ChallengeDto> findByUserId(Long userId) {
        return challengeQueryRepository.findByUserId(userId);
    }

    @Override
    public Challenge save(Challenge challenge) {
        return challengeJpaRepository.save(challenge);
    }

    @Override
    public Optional<ChallengeDto> findProgressingChallengeByUserId(Long userId) {
        return challengeQueryRepository.findProgressingChallengeByUserId(userId);
    }

    @Override
    public Page<ChallengeDto> findByUserIdAndStatus(Long userId, ChallengeStatus status, Pageable pageable) {
        return challengeQueryRepository.findByUserIdAndStatus(userId, status, pageable);
    }
}
