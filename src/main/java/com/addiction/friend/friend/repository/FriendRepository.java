package com.addiction.friend.friend.repository;

import com.addiction.friend.friend.repository.response.FriendProfileDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FriendRepository {

    Page<FriendProfileDto> getFriendList(Long userId, Pageable pageable);

}
