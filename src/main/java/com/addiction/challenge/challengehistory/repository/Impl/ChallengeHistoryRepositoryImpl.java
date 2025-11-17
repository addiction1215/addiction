package com.addiction.challenge.challengehistory.repository.Impl;

import com.addiction.challenge.challengehistory.repository.ChallengeHistoryJpaRepository;
import com.addiction.challenge.challengehistory.repository.ChallengeHistoryQueryRepository;
import com.addiction.challenge.challengehistory.repository.ChallengeHistoryRepository;
import com.addiction.challenge.challengehistory.repository.response.ChallengeHistoryUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChallengeHistoryRepositoryImpl implements ChallengeHistoryRepository {

    private final ChallengeHistoryJpaRepository challengeHistoryJpaRepository;
    private final ChallengeHistoryQueryRepository challengeHistoryQueryRepository;

    @Override
    public void deleteAllInBatch() {
        challengeHistoryJpaRepository.deleteAllInBatch();
    }

    @Override
    public List<ChallengeHistoryUserDto> findByUserId(Long userId) {
        return challengeHistoryQueryRepository.findByUserId(userId);
    }
}
