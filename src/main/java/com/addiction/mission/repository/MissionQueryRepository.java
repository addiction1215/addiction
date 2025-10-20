package com.addiction.mission.repository;

import com.addiction.mission.service.mission.response.MissionResponseList;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.addiction.challenge.entity.QChallenge.challenge;
import static com.addiction.mission.entity.QMission.mission;

@RequiredArgsConstructor
@Repository
public class MissionQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public List<MissionResponseList> findByChallengeIdAndUserId(Long challengeId, long userId) {
        JPAQuery<MissionResponseList> query = jpaQueryFactory.select(Projections.constructor(
                        MissionResponseList.class,
                        mission.id,
                        mission.challengeId,
                        mission.category,
                        mission.title,
                        mission.reward,
                        mission.content
                ))
                .from(mission)
                .join(challenge)
                .on(mission.challengeId.id.eq(challengeId))
                .where(mission.userId.id.eq(userId));

        return query.fetch();
    }
}
