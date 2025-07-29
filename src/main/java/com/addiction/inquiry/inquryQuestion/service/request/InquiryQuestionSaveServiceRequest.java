package com.addiction.inquiry.inquryQuestion.service.request;

import com.addiction.inquiry.inquryQuestion.entity.InquiryQuestion;
import lombok.Builder;
import lombok.Getter;

@Getter
public class InquiryQuestionSaveServiceRequest {

    private final String title;
    private final String question;

    @Builder
    public InquiryQuestionSaveServiceRequest(String question, String title) {
        this.question = question;
        this.title = title;
    }

    public InquiryQuestion toEntity() {
        return InquiryQuestion.builder()
                .title(title)
                .question(question)
                .build();
    }
}
