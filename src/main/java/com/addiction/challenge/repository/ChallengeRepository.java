package com.addiction.challenge.repository;

import com.addiction.challenge.entity.Challenge;
import com.addiction.common.enums.YnStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChallengeRepository {
    Page<Challenge> findByFinishYnAndUserId(YnStatus finishYn, long userId, Pageable pageable);
    Challenge save(Challenge challenge);
}
