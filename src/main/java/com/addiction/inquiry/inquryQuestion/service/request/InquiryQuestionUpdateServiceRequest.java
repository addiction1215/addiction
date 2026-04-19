package com.addiction.inquiry.inquryQuestion.service.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class InquiryQuestionUpdateServiceRequest {

    private final Long id;
    private final String question;

    @Builder
    public InquiryQuestionUpdateServiceRequest(Long id, String question) {
        this.id = id;
        this.question = question;
    }
}
