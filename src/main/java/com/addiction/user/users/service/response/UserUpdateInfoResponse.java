package com.addiction.user.users.service.response;

import com.addiction.user.users.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserUpdateInfoResponse {

    private final String phoneNumber;
    private final String email;

    @Builder
    public UserUpdateInfoResponse(String email, String phoneNumber) {
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public static UserUpdateInfoResponse createResponse(User user) {
        return UserUpdateInfoResponse.builder()
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
