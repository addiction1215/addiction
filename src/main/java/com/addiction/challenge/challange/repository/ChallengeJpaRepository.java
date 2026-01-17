package com.addiction.challenge.challange.repository;

import com.addiction.challenge.challange.entity.Challenge;
import com.addiction.challenge.mission.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeJpaRepository extends JpaRepository<Challenge, Long> {
}
