package com.addiction.user.users.service.response;

import com.addiction.user.users.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserUpdateInfoResponse {

    private final String password;
    private final String phoneNumber;
    private final String email;

    @Builder
    public UserUpdateInfoResponse(String email, String password, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public static UserUpdateInfoResponse createResponse(User user) {
        return UserUpdateInfoResponse.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
