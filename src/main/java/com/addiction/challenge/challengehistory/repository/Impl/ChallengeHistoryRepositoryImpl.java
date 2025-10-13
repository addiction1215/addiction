package com.addiction.challenge.challengehistory.repository.Impl;

import com.addiction.challenge.challengehistory.repository.ChallengeHistoryJpaRepository;
import com.addiction.challenge.challengehistory.repository.ChallengeHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChallengeHistoryRepositoryImpl implements ChallengeHistoryRepository {

    private final ChallengeHistoryJpaRepository challengeHistoryJpaRepository;

    @Override
    public void deleteAllInBatch() {
        challengeHistoryJpaRepository.deleteAllInBatch();
    }
}
