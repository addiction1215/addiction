package com.addiction.inquiry.inquryQuestion.service.response;

import com.addiction.inquiry.inquryQuestion.entity.InquiryQuestion;
import com.addiction.inquiry.inquryQuestion.enums.InquiryStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class InquiryQuestionFindResponse {

    private final String title;
    private final InquiryStatus inquiryStatus;

    @Builder
    public InquiryQuestionFindResponse(String title, InquiryStatus inquiryStatus) {
        this.title = title;
        this.inquiryStatus = inquiryStatus;
    }

    public static InquiryQuestionFindResponse createResponse(InquiryQuestion inquiryQuestion) {
        return InquiryQuestionFindResponse.builder()
                .title(inquiryQuestion.getTitle())
                .inquiryStatus(inquiryQuestion.getInquiryStatus())
                .build();
    }
}
