package com.addiction.user.users.service.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SendAuthCodeResponse {

    private final Long id;

    @Builder
    private SendAuthCodeResponse(Long id) {
        this.id = id;
    }

    public static SendAuthCodeResponse createResponse(Long id) {
        return SendAuthCodeResponse.builder()
                .id(id)
                .build();
    }

}
