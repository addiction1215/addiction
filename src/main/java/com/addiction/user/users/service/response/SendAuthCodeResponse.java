package com.addiction.user.users.service.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SendAuthCodeResponse {

    private final String authCode;

    @Builder
    private SendAuthCodeResponse(String authCode) {
        this.authCode = authCode;
    }

    public static SendAuthCodeResponse createResponse(String authCode){
        return SendAuthCodeResponse.builder()
            .authCode(authCode)
            .build();
    }

}
