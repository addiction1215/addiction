package com.addiction.challenge.repository;

import com.addiction.challenge.entity.Challenge;
import com.addiction.common.enums.YnStatus;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.addiction.challenge.entity.QChallenge.challenge;
import static com.addiction.challengehistory.entity.QChallengeHistory.challengeHistory;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Repository
public class ChallengeQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public Page<Challenge> findByFinishYnAndUserId(YnStatus finishYn, long userId, Pageable pageable) {
        JPAQuery<Challenge> query = jpaQueryFactory
                .select(challenge)
                .from(challenge);

        if (YnStatus.Y.equals(finishYn)) {
            query.innerJoin(challengeHistory)
                    .on(challengeHistory.challengeId.id.eq(challenge.id))
                    .where(challengeHistory.userId.id.eq(userId));
        }

        List<Challenge> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = getTotalChallengeCount(finishYn, userId);

        return new PageImpl<>(content, pageable, total);
    }

    private long getTotalChallengeCount(YnStatus finishYn, long userId) {
        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(challenge.count())
                .from(challenge);

        if (YnStatus.Y.equals(finishYn)) {
            countQuery.innerJoin(challengeHistory)
                    .on(challengeHistory.challengeId.id.eq(challenge.id))
                    .where(challengeHistory.userId.id.eq(userId));
        }

        return ofNullable(countQuery.fetchOne()).orElse(0L);
    }
}
