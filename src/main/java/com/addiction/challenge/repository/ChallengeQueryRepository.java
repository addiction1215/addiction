package com.addiction.challenge.repository;

import com.addiction.challenge.service.challenge.response.ChallengeResponseList;
import com.addiction.common.enums.ChallengeStatus;
import com.addiction.common.enums.YnStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.EnumExpression;
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
        EnumExpression<ChallengeStatus> status = challengeHistory.status.coalesce(ChallengeStatus.PROGRESSING);

        JPAQuery<ChallengeResponseList> query = jpaQueryFactory
                .select(Projections.constructor(ChallengeResponseList.class,
                        challenge.id,
                        challenge.title,
                        challenge.content,
                        challenge.badge,
                        status
                ))
                .from(challenge)
                .leftJoin(challengeHistory)
                .on(challenge.id.eq(challengeHistory.challengeId.id)
                        .and(challengeHistory.userId.id.eq(userId)));

        return query.fetch();
    }
}
