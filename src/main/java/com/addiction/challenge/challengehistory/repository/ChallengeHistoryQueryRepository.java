package com.addiction.challenge.challengehistory.repository;

import com.addiction.challenge.challange.entity.Challenge;
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
     * 사용 중이며, 사용자의 챌린지 이력이 없는 챌린지를 조회한다.
     */
    public Page<Challenge> findLeftChallenges(Long userId, Pageable pageable) {
        // 사용자가 챌린지 이력을 생성한 챌린지 ID 목록 조회
        List<Long> challengeHistoryIds = jpaQueryFactory
                .select(challengeHistory.challenge.id)
                .from(challengeHistory)
                .where(
                        challengeHistory.user.id.eq(userId)
                )
                .fetch();

        // 사용 중이고 이력이 없는 챌린지 조회
        List<Challenge> content = jpaQueryFactory
                .selectFrom(challenge)
                .where(
                        challenge.useYn.eq("Y"),
                        challenge.id.notIn(challengeHistoryIds)
                )
                .orderBy(challenge.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 개수
        Long total = jpaQueryFactory
                .select(challenge.count())
                .from(challenge)
                .where(
                        challenge.useYn.eq("Y"),
                        challenge.id.notIn(challengeHistoryIds)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }
}
