package com.addiction.user.users.service.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class VerifyAuthCodeResponse {

    private final String message;

    @Builder
    private VerifyAuthCodeResponse(String message) {
        this.message = message;
    }

    public static VerifyAuthCodeResponse success() {
        return VerifyAuthCodeResponse.builder()
                .message("이메일 인증이 완료되었습니다.")
                .build();
    }
}
