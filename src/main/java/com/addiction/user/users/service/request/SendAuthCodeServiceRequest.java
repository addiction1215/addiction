package com.addiction.user.users.service.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SendAuthCodeServiceRequest {

    private final String email;

    @Builder
    private SendAuthCodeServiceRequest(String email) {
        this.email = email;
    }
}
