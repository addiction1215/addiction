package com.addiction.challenge.challenge.repository;

import com.addiction.challenge.challenge.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeJpaRepository extends JpaRepository<Challenge, Long> {
}
