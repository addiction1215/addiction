package com.addiction.challenge.repository;

import com.addiction.challenge.entity.Challenge;
import com.addiction.challenge.repository.response.ChallengeDto;
import com.addiction.challenge.service.challenge.response.ChallengeResponse;

import java.util.List;

public interface ChallengeRepository {
    List<ChallengeDto> findByUserId(Long userId);

    Challenge save(Challenge challenge);
}
