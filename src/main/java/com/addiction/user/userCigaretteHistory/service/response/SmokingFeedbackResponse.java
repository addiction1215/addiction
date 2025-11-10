package com.addiction.user.userCigaretteHistory.service.response;

import com.addiction.user.userCigaretteHistory.enums.SmokingFeedback;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SmokingFeedbackResponse {

    private String message;

    public static SmokingFeedbackResponse createResponse(SmokingFeedback feedback) {
        return SmokingFeedbackResponse.builder()
                .message(feedback.getMessage())
                .build();
    }
}
