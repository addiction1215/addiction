package com.addiction.challenge.challengehistory.repository;

import com.addiction.challenge.challange.entity.Challenge;
import com.addiction.challenge.challengehistory.policy.ChallengeParticipationPolicy;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.addiction.challenge.challange.entity.QChallenge.challenge;
import static com.addiction.challenge.challengehistory.entity.QChallengeHistory.challengeHistory;


@RequiredArgsConstructor
@Repository
public class ChallengeHistoryQueryRepository {
    
    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 사용 중이며, 사용자의 재참여를 차단하는 이력이 없는 챌린지를 조회한다.
     */
    public Page<Challenge> findLeftChallenges(Long userId, Pageable pageable) {
        List<Challenge> content = jpaQueryFactory
                .selectFrom(challenge)
                .where(
                        challenge.useYn.eq("Y"),
                        JPAExpressions
                                .selectOne()
                                .from(challengeHistory)
                                .where(
                                        challengeHistory.user.id.eq(userId),
                                        challengeHistory.challenge.id.eq(challenge.id),
                                        challengeHistory.status.in(
                                                ChallengeParticipationPolicy.REJOIN_BLOCKING_STATUSES)
                                )
                                .notExists()
                )
                .orderBy(challenge.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(challenge.count())
                .from(challenge)
                .where(
                        challenge.useYn.eq("Y"),
                        JPAExpressions
                                .selectOne()
                                .from(challengeHistory)
                                .where(
                                        challengeHistory.user.id.eq(userId),
                                        challengeHistory.challenge.id.eq(challenge.id),
                                        challengeHistory.status.in(
                                                ChallengeParticipationPolicy.REJOIN_BLOCKING_STATUSES)
                                )
                                .notExists()
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }
}
