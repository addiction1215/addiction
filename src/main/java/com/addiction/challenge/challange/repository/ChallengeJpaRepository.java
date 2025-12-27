package com.addiction.challenge.challange.repository;

import com.addiction.challenge.challange.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeJpaRepository extends JpaRepository<Challenge, Long> {
}
