package com.addiction.user.userCigaretteHistory.service.response;

import com.addiction.user.userCigaretteHistory.enums.StatsFeedback;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StatsFeedbackResponse {

    private String message;

    public static StatsFeedbackResponse createResponse(StatsFeedback feedback) {
        return StatsFeedbackResponse.builder()
                .message(feedback.getMessage())
                .build();
    }
}
