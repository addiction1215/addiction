package com.addiction.user.users.controller.request;

import com.addiction.user.users.service.request.SendAuthCodeServiceRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendAuthCodeRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    public SendAuthCodeServiceRequest toServiceRequest(){
        return SendAuthCodeServiceRequest.builder()
                .email(email)
                .build();
    }

    @Builder
    public SendAuthCodeRequest(String email) {
        this.email = email;
    }
}
