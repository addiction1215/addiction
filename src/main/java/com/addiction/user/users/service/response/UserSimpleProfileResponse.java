package com.addiction.user.users.service.response;

import com.addiction.user.users.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSimpleProfileResponse {

    private final String profileUrl;
    private final String email;
    private final String nickName;

    @Builder
    public UserSimpleProfileResponse(String profileUrl, String email, String nickName) {
        this.profileUrl = profileUrl;
        this.email = email;
        this.nickName = nickName;
    }

    public static UserSimpleProfileResponse createResponse(User user) {
        return UserSimpleProfileResponse.builder()
                .profileUrl(user.getProfileUrl())
                .email(user.getEmail())
                .nickName(user.getNickName())
                .build();
    }
}
