package com.addiction.challenge.repository;

import com.addiction.challenge.repository.response.ChallengeDto;
import com.addiction.common.enums.ChallengeStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.addiction.challenge.entity.QChallenge.challenge;
import static com.addiction.challengehistory.entity.QChallengeHistory.challengeHistory;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Repository
public class ChallengeQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public List<ChallengeDto> findByUserId(Long userId) {
        JPAQuery<ChallengeDto> query = jpaQueryFactory
                .select(Projections.constructor(ChallengeDto.class,
                        challenge.id,
                        challenge.title,
                        challenge.content,
                        challenge.badge,
                        challengeHistory.status.coalesce(ChallengeStatus.PROGRESSING)
                ))
                .from(challenge)
                .leftJoin(challengeHistory)
                .on(challenge.id.eq(challengeHistory.challenge.id)
                        .and(challengeHistory.user.id.eq(userId)));

        return query.fetch();
    }

    /**
     * 진행중인 챌린지 1개 조회 (최신순, LIMIT 1)
     */
    public Optional<ChallengeDto> findProgressingChallengeByUserId(Long userId) {
        ChallengeDto result = jpaQueryFactory
                .select(Projections.constructor(ChallengeDto.class,
                        challenge.id,
                        challenge.title,
                        challenge.content,
                        challenge.badge,
                        challengeHistory.status
                ))
                .from(challengeHistory)
                .innerJoin(challenge).on(challengeHistory.challenge.id.eq(challenge.id))
                .where(
                        challengeHistory.user.id.eq(userId),
                        challengeHistory.status.eq(ChallengeStatus.PROGRESSING)
                )
                .orderBy(challengeHistory.createdDate.desc())
                .limit(1)
                .fetchOne();

        return Optional.ofNullable(result);
    }

    /**
     * 특정 상태의 챌린지 목록 조회 (페이징 지원)
     */
    public Page<ChallengeDto> findByUserIdAndStatus(Long userId, ChallengeStatus status, Pageable pageable) {
        List<ChallengeDto> content = jpaQueryFactory
                .select(Projections.constructor(ChallengeDto.class,
                        challenge.id,
                        challenge.title,
                        challenge.content,
                        challenge.badge,
                        challengeHistory.status
                ))
                .from(challengeHistory)
                .innerJoin(challenge).on(challengeHistory.challenge.id.eq(challenge.id))
                .where(
                        challengeHistory.user.id.eq(userId),
                        challengeHistory.status.eq(status)
                )
                .orderBy(challengeHistory.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, getTotalChallengeCount(userId, status));
    }

    public Long getTotalChallengeCount(Long userId, ChallengeStatus status) {
        return ofNullable(
                jpaQueryFactory
                        .select(challengeHistory.count())
                        .from(challengeHistory)
                        .where(
                                challengeHistory.user.id.eq(userId),
                                challengeHistory.status.eq(status)
                        )
                        .fetchOne()
        ).orElse(0L);
    }
}
