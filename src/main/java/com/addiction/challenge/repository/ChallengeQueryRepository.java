package com.addiction.challenge.repository;

import com.addiction.challenge.service.challenge.response.ChallengeResponseList;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.addiction.challenge.entity.QChallenge.challenge;
import static com.addiction.challengehistory.entity.QChallengeHistory.challengeHistory;

@RequiredArgsConstructor
@Repository
public class ChallengeQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public List<ChallengeResponseList> findByUserId(long userId) {
        JPAQuery<ChallengeResponseList> query = jpaQueryFactory
                .select(Projections.constructor(ChallengeResponseList.class,
                        challenge,
                        challengeHistory.finishYn
                ))
                .from(challenge)
                .innerJoin(challengeHistory)
                .on(challengeHistory.challengeId.id.eq(challenge.id))
                .where(challengeHistory.userId.id.eq(userId));

        return query.fetch();
    }
}
