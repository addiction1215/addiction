package com.addiction.friend.repository.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FriendProfileDto {
    private Long id;
    private Long friendId;
    private String nickname;
    private String email;

    public FriendProfileDto(Long id, Long friendId, String nickname) {
        this.id = id;
        this.friendId = friendId;
        this.nickname = nickname;
    }

    public FriendProfileDto(Long id, Long friendId, String nickname, String email) {
        this.id = id;
        this.friendId = friendId;
        this.nickname = nickname;
        this.email = email;
    }
}