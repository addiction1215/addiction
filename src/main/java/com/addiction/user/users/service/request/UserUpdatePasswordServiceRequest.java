package com.addiction.user.users.service.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserUpdatePasswordServiceRequest {

    private final String currentPassword;
    private final String newPassword;
    private final String newPasswordConfirm;

    @Builder
    public UserUpdatePasswordServiceRequest(String currentPassword, String newPassword, String newPasswordConfirm) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.newPasswordConfirm = newPasswordConfirm;
    }
}
