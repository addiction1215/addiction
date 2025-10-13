package com.addiction.friend.repository.response;

import lombok.Getter;

@Getter
public class FriendProfileDto {
    private Long id;
    private Long friendId;
    private String nickname;

    public FriendProfileDto(Long id, Long friendId, String nickname) {
        this.id = id;
        this.friendId = friendId;
        this.nickname = nickname;
    }
}