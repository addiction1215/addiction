package com.addiction.friend.repository.response;

import lombok.Getter;

@Getter
public class FriendProfileDto {
    private Long friendId;
    private String nickname;

    public FriendProfileDto(Long friendId, String nickname) {
        this.friendId = friendId;
        this.nickname = nickname;
    }
}