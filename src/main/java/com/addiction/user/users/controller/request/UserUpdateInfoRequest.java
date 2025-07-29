package com.addiction.user.users.controller.request;

import com.addiction.user.users.service.request.UserUpdateInfoServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateInfoRequest {

    @NotNull(message = "비밀번호는 필수입니다.")
    private String password;
    @NotNull(message = "핸드폰번호는 필수입니다.")
    private String phoneNumber;
    @NotNull(message = "이메일은 필수입니다.")
    private String email;

    @Builder
    public UserUpdateInfoRequest(String email, String password, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public UserUpdateInfoServiceRequest toServiceRequest() {
        return UserUpdateInfoServiceRequest.builder()
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .build();
    }
}
