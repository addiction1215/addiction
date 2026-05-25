package com.addiction.user.users.controller.request;

import com.addiction.user.users.service.request.UserUpdatePasswordServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdatePasswordRequest {

    @NotBlank(message = "현재 비밀번호는 필수입니다.")
    private String currentPassword;

    @NotBlank(message = "새 비밀번호는 필수입니다.")
    private String newPassword;

    @NotBlank(message = "새 비밀번호 확인은 필수입니다.")
    private String newPasswordConfirm;

    @Builder
    public UserUpdatePasswordRequest(String currentPassword, String newPassword, String newPasswordConfirm) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.newPasswordConfirm = newPasswordConfirm;
    }

    public UserUpdatePasswordServiceRequest toServiceRequest() {
        return UserUpdatePasswordServiceRequest.builder()
                .currentPassword(currentPassword)
                .newPassword(newPassword)
                .newPasswordConfirm(newPasswordConfirm)
                .build();
    }
}
