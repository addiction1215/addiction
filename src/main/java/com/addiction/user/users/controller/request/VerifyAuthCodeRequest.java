package com.addiction.user.users.controller.request;

import com.addiction.user.users.service.request.VerifyAuthCodeServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VerifyAuthCodeRequest {

    @NotNull(message = "인증 ID는 필수입니다.")
    private Long id;

    @NotBlank(message = "인증번호는 필수입니다.")
    private String authCode;

    public VerifyAuthCodeServiceRequest toServiceRequest() {
        return VerifyAuthCodeServiceRequest.builder()
                .id(id)
                .authCode(authCode)
                .build();
    }

    @Builder
    public VerifyAuthCodeRequest(Long id, String authCode) {
        this.id = id;
        this.authCode = authCode;
    }
}
