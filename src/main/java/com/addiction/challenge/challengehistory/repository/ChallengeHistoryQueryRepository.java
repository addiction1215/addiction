package com.addiction.challenge.challengehistory.repository;

import com.addiction.challenge.challange.entity.Challenge;
import com.addiction.challenge.challange.entity.QChallenge;
import com.addiction.challenge.challengehistory.entity.QChallengeHistory;
import com.addiction.common.enums.ChallengeStatus;
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
     * 남은 챌린지 조회 (사용자가 완료하지 않은 챌린지들)
     * Challenge 전체에서 해당 사용자의 ChallengeHistory에 COMPLETED가 아닌 것들
     */
    public Page<Challenge> findLeftChallenges(Long userId, Pageable pageable) {
        // 사용자가 완료한 챌린지 ID 목록 조회
        List<Long> completedChallengeIds = jpaQueryFactory
                .select(challengeHistory.challenge.id)
                .from(challengeHistory)
                .where(
                        challengeHistory.user.id.eq(userId)
                )
                .fetch();

        // 완료하지 않은 챌린지 조회
        List<Challenge> content = jpaQueryFactory
                .selectFrom(challenge)
                .where(challenge.id.notIn(completedChallengeIds))
                .orderBy(challenge.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 개수
        Long total = jpaQueryFactory
                .select(challenge.count())
                .from(challenge)
                .where(challenge.id.notIn(completedChallengeIds))
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }
}
