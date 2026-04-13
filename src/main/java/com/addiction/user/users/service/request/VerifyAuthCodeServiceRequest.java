package com.addiction.user.users.service.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class VerifyAuthCodeServiceRequest {

    private final Long id;
    private final String authCode;

    @Builder
    private VerifyAuthCodeServiceRequest(Long id, String authCode) {
        this.id = id;
        this.authCode = authCode;
    }
}
