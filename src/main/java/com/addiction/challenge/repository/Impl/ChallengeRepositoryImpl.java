package com.addiction.challenge.repository.Impl;

import com.addiction.challenge.entity.Challenge;
import com.addiction.challenge.repository.ChallengeJpaRepository;
import com.addiction.challenge.repository.ChallengeQueryRepository;
import com.addiction.challenge.repository.ChallengeRepository;
import com.addiction.challenge.service.challenge.response.ChallengeResponseList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
