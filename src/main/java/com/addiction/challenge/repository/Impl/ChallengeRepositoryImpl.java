package com.addiction.challenge.repository.Impl;

import com.addiction.challenge.entity.Challenge;
import com.addiction.challenge.repository.ChallengeJpaRepository;
import com.addiction.challenge.repository.ChallengeQueryRepository;
import com.addiction.challenge.repository.ChallengeRepository;
import com.addiction.common.enums.YnStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChallengeRepositoryImpl implements ChallengeRepository {
    private final ChallengeQueryRepository challengeQueryRepository;
    private final ChallengeJpaRepository challengeJpaRepository;

    @Override
    public Page<Challenge> findByFinishYnAndUserId(YnStatus finishYn, long userId, Pageable pageable) {
        return challengeQueryRepository.findByFinishYnAndUserId(finishYn, userId, pageable);
    }

    @Override
    public Challenge save(Challenge challenge) {
        return challengeJpaRepository.save(challenge);
    }
}
