package com.addiction.challenge.repository;

import com.addiction.challenge.entity.Challenge;
import com.addiction.challenge.service.challenge.response.ChallengeResponseList;

import java.util.List;

public interface ChallengeRepository {
    List<ChallengeResponseList> findByUserId(long userId);

    Challenge save(Challenge challenge);
}
