package com.addiction.friend.repository;

import com.addiction.friend.repository.response.FriendProfileDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.addiction.friend.entity.QFriend.friend;
import static com.addiction.user.users.entity.QUser.user;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Repository
@Slf4j
public class FriendQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<FriendProfileDto> getFriendList(Long userId, Pageable pageable) {
        List<FriendProfileDto> content = queryFactory
                .select(Projections.constructor(FriendProfileDto.class,
                        friend.id,
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

    public Page<FriendProfileDto> getBlockedFriendList(Long userId, Pageable pageable) {
        List<FriendProfileDto> content = queryFactory
                .select(Projections.constructor(FriendProfileDto.class,
                        friend.id,
                        friend.receiver.id,
                        friend.receiver.nickName
                ))
                .from(friend)
                .join(friend.receiver, user)
                .where(
                        isParticipant(userId),
                        friend.status.eq(com.addiction.friend.entity.FriendStatus.BLOCKED)
                )
                .orderBy(friend.receiver.nickName.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        log.info("getBlockedFriendList userId: {}, pageable: {}, content: {}", userId, pageable, content);

        long total = getTotalBlockedFriendsCount(userId);
        log.info("getBlockedFriendList userId: {}, total: {}", userId, total);

        return new PageImpl<>(content, pageable, total);
    }

    public Page<FriendProfileDto> searchFriends(Long userId, String keyword, Pageable pageable) {
        List<FriendProfileDto> content = queryFactory
                .select(Projections.constructor(FriendProfileDto.class,
                        0L,
                        user.id,
                        user.nickName
                ))
                .from(user)
                .where(
                        user.id.ne(userId),
                        user.email.containsIgnoreCase(keyword),
                        user.id.notIn(
                                queryFactory
                                        .select(friend.receiver.id)
                                        .from(friend)
                                        .where(friend.requester.id.eq(userId))
                        )
                )
                .orderBy(user.nickName.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = getSearchFriendsCount(userId, keyword);

        return new PageImpl<>(content, pageable, total);
    }

    private long getSearchFriendsCount(Long userId, String keyword) {
        return ofNullable(
                queryFactory
                        .select(user.count())
                        .from(user)
                        .where(
                                user.id.ne(userId),
                                user.email.containsIgnoreCase(keyword),
                                user.id.notIn(
                                        queryFactory
                                                .select(friend.receiver.id)
                                                .from(friend)
                                                .where(friend.requester.id.eq(userId))
                                )
                        )
                        .fetchOne()
        ).orElse(0L);
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

    private long getTotalBlockedFriendsCount(Long userId) {
        return ofNullable(
                queryFactory
                        .select(friend.count())
                        .from(friend)
                        .where(
                                isParticipant(userId),
                                friend.status.eq(com.addiction.friend.entity.FriendStatus.BLOCKED)
                        )
                        .fetchOne()
        ).orElse(0L);
    }

    private BooleanExpression isRequesterIdEqualTo(Long userId) {
        return friend.requester.id.eq(userId);
    }

    private BooleanExpression isParticipant(Long userId) {
        return friend.requester.id.eq(userId).or(friend.receiver.id.eq(userId));
    }
}
