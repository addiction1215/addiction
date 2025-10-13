package com.addiction.challenge.repository;

import com.addiction.challenge.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeJpaRepository extends JpaRepository<Challenge, Long> {
}
