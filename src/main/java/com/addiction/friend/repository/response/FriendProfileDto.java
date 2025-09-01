package com.addiction.friend.repository.response;

import lombok.Getter;

@Getter
public class FriendProfileDto {
    private int friendId;
    private String nickname;

    public FriendProfileDto(int friendId, String nickname) {
        this.friendId = friendId;
        this.nickname = nickname;
    }
}