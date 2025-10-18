package com.addiction.user.users.service.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FindPasswordServiceRequest {

    private final String email;
    private final String nickName;

    @Builder
    private FindPasswordServiceRequest(String email, String nickName) {
        this.email = email;
        this.nickName = nickName;
    }
}