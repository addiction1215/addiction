package com.addiction.user.users.service.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FindPasswordResponse {

    private final String message;

    @Builder
    private FindPasswordResponse(String message) {
        this.message = message;
    }

    public static FindPasswordResponse of(String email) {
        return FindPasswordResponse.builder()
                .message(email + "로 임시 비밀번호가 전송되었습니다.")
                .build();
    }
}