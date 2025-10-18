package com.addiction.user.users.service.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class SendMailRequest {
    private final String email;
    private final String subject;
    private final String text;

    @Builder
    private SendMailRequest(String email, String subject, String text) {
        this.email = email;
        this.subject = subject;
        this.text = text;
    }
}
