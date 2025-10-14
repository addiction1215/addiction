package com.addiction.challenge.challenge.repository;

import com.addiction.challenge.challenge.entity.Challenge;
import com.addiction.challenge.challenge.service.challenge.response.ChallengeResponseList;

import java.util.List;
import java.util.Optional;

public interface ChallengeRepository {
    List<ChallengeResponseList> findByUserId(long userId);

    Challenge save(Challenge challenge);

    void deleteAllInBatch();

    Optional<Challenge> findById(Long challengeId);
}
