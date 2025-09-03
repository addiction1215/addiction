package com.addiction.friend.repository;

import com.addiction.friend.repository.response.FriendProfileDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FriendRepository {

    Page<FriendProfileDto> getFriendList(Long userId, Pageable pageable);
    Page<FriendProfileDto> getBlockedFriendList(Long userId, Pageable pageable);

}
