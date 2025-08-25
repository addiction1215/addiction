package com.addiction.friend.friend.repository;

import com.addiction.friend.friend.entity.QFriend;
import com.addiction.friend.friend.repository.response.FriendProfileDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.addiction.friend.friend.entity.QFriend.friend;
import static com.addiction.user.users.entity.QUser.user;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Repository
public class FriendQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<FriendProfileDto> getFriendList(Long userId, Pageable pageable) {
        List<FriendProfileDto> content = queryFactory
                .select(Projections.constructor(FriendProfileDto.class,
                        friend.receiver.id,
                        friend.receiver.nickName
                ))
                .from(friend)
                .join(friend.receiver, user)
                .where(
                        isRequesterIdEqualTo(userId)
                )
                .orderBy(friend.receiver.nickName.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = getTotalFriendsCount(userId);

        return new PageImpl<>(content, pageable, total);
    }

    private long getTotalFriendsCount(Long userId) {
        return ofNullable(
                queryFactory
                        .select(friend.count())
                        .from(friend)
                        .where(
                                isRequesterIdEqualTo(userId)
                        )
                        .fetchOne()
        ).orElse(0L);
    }

    private BooleanExpression isRequesterIdEqualTo(Long userId) {
        return friend.requester.id.eq(userId);
    }
}
