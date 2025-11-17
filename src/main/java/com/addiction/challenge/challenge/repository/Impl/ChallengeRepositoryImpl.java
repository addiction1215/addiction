package com.addiction.challenge.challenge.repository.Impl;

import com.addiction.challenge.challenge.entity.Challenge;
import com.addiction.challenge.challenge.repository.ChallengeJpaRepository;
import com.addiction.challenge.challenge.repository.ChallengeQueryRepository;
import com.addiction.challenge.challenge.repository.ChallengeRepository;
import com.addiction.challenge.challenge.service.challenge.response.ChallengeResponseList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChallengeRepositoryImpl implements ChallengeRepository {
    private final ChallengeQueryRepository challengeQueryRepository;
    private final ChallengeJpaRepository challengeJpaRepository;

    @Override
    public List<ChallengeResponseList> findByUserId(long userId) {
        return challengeQueryRepository.findByUserId(userId);
    }

    @Override
    public Challenge save(Challenge challenge) {
        return challengeJpaRepository.save(challenge);
    }

    @Override
    public void deleteAllInBatch() {
        challengeJpaRepository.deleteAllInBatch();
    }

    @Override
    public Optional<Challenge> findById(Long challengeId) {
        return challengeJpaRepository.findById(challengeId);
    }
}
