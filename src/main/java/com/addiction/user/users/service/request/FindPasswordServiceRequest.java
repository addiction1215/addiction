package com.addiction.user.users.service.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FindPasswordServiceRequest {

    private final String email;

    @Builder
    private FindPasswordServiceRequest(String email) {
        this.email = email;
    }
}
