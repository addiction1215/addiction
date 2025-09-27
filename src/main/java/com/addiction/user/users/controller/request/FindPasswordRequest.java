package com.addiction.user.users.controller.request;

import com.addiction.user.users.service.request.FindPasswordServiceRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindPasswordRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickName;

    @Builder
    private FindPasswordRequest(String email, String nickName) {
        this.email = email;
        this.nickName = nickName;
    }

    public FindPasswordServiceRequest toServiceRequest() {
        return FindPasswordServiceRequest.builder()
                .email(email)
                .nickName(nickName)
                .build();
    }
}