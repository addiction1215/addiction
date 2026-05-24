package com.addiction.user.users.service.response;

import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SnsType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSimpleProfileResponse {

    private final String profileUrl;
    private final String email;
    private final String nickName;
    private final SnsType snsType;

    @Builder
    public UserSimpleProfileResponse(String profileUrl, String email, String nickName, SnsType snsType) {
        this.profileUrl = profileUrl;
        this.email = email;
        this.nickName = nickName;
        this.snsType = snsType;
    }

    public static UserSimpleProfileResponse createResponse(User user, String profileUrl) {
        return UserSimpleProfileResponse.builder()
                .profileUrl(profileUrl)
                .email(user.getEmail())
                .nickName(user.getNickName())
                .snsType(user.getSnsType())
                .build();
    }
}
