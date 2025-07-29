package com.addiction.user.users.service.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class UserUpdateInfoServiceRequest {

    private final String password;
    private final String phoneNumber;
    private final String email;

    @Builder
    public UserUpdateInfoServiceRequest(String email, String password, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }
}
