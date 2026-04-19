package com.addiction.inquiry.inquryQuestion.controller.request;

import com.addiction.inquiry.inquryQuestion.service.request.InquiryQuestionUpdateServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InquiryQuestionUpdateRequest {

    @NotNull(message = "문의 내용은 필수입니다.")
    private String question;

    @Builder
    public InquiryQuestionUpdateRequest(String question) {
        this.question = question;
    }

    public InquiryQuestionUpdateServiceRequest toServiceRequest(Long id) {
        return InquiryQuestionUpdateServiceRequest.builder()
                .id(id)
                .question(question)
                .build();
    }
}
