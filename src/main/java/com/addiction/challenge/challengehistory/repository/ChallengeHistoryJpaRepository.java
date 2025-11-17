package com.addiction.challenge.challengehistory.repository;

import com.addiction.challenge.challengehistory.entity.ChallengeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeHistoryJpaRepository extends JpaRepository<ChallengeHistory, Long> {
}
