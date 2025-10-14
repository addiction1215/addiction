package com.addiction.challenge.challengehistory.repository;

import com.addiction.challenge.challengehistory.repository.response.ChallengeHistoryUserDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.addiction.challenge.challenge.entity.QChallenge.challenge;
import static com.addiction.challenge.challengehistory.entity.QChallengeHistory.challengeHistory;

@RequiredArgsConstructor
@Repository
public class ChallengeHistoryQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public List<ChallengeHistoryUserDto> findByUserId(Long userId) {
        return jpaQueryFactory
                .select(Projections.constructor(ChallengeHistoryUserDto.class,
                        challenge
                ))
                .from(challenge)
                .innerJoin(challengeHistory)
                .on(challengeHistory.challenge.id.eq(challenge.id))
                .where(challengeHistory.user.id.eq(userId))
                .orderBy(challengeHistory.createdDate.desc())
                .fetch();
    }
}
