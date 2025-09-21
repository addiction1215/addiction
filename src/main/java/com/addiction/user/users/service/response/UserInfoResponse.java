package com.addiction.user.users.service.response;

import com.addiction.user.users.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfoResponse {

    private final String phoneNumber;
    private final String email;

    @Builder
    public UserInfoResponse(String email, String phoneNumber) {
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public static UserInfoResponse createResponse(User user) {
        return UserInfoResponse.builder()
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
