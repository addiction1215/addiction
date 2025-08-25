package com.addiction.friend.friend.repository.impl;

import com.addiction.friend.friend.repository.FriendQueryRepository;
import com.addiction.friend.friend.repository.FriendRepository;
import com.addiction.friend.friend.repository.response.FriendProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FriendRepositoryImpl implements FriendRepository {

    private final FriendQueryRepository friendQueryRepository;

    @Override
    public Page<FriendProfileDto> getFriendList(Long userId, Pageable pageable) {
        return friendQueryRepository.getFriendList(userId, pageable);
    }
}
