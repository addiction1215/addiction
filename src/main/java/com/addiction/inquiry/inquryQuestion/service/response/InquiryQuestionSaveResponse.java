package com.addiction.inquiry.inquryQuestion.service.response;

import com.addiction.inquiry.inquryQuestion.entity.InquiryQuestion;
import lombok.Builder;
import lombok.Getter;

@Getter
public class InquiryQuestionSaveResponse {

    private final Long userId;
    private final String title;
    private final String question;

    @Builder
    public InquiryQuestionSaveResponse(String question, Long userId, String title) {
        this.question = question;
        this.userId = userId;
        this.title = title;
    }

    public static InquiryQuestionSaveResponse createResponse(InquiryQuestion inquiryQuestion) {
        return InquiryQuestionSaveResponse.builder()
                .question(inquiryQuestion.getQuestion())
                .userId(inquiryQuestion.getUser().getId())
                .title(inquiryQuestion.getTitle())
                .build();
    }
}
