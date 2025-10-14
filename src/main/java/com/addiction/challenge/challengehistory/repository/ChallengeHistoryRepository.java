package com.addiction.challenge.challengehistory.repository;


import com.addiction.challenge.challengehistory.repository.response.ChallengeHistoryUserDto;

import java.util.List;

public interface ChallengeHistoryRepository {

    void deleteAllInBatch();

    List<ChallengeHistoryUserDto> findByUserId(Long userId);

}
