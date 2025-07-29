package com.addiction.inquiry.inquryQuestion.controller.request;

import com.addiction.inquiry.inquryQuestion.service.request.InquiryQuestionSaveServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InquiryQuestionSaveRequest {

    @NotNull(message = "제목은 필수입니다.")
    private String title;
    @NotNull(message = "질문 내용은 필수입니다.")
    private String question;

    @Builder
    public InquiryQuestionSaveRequest(String question, String title) {
        this.question = question;
        this.title = title;
    }

    public InquiryQuestionSaveServiceRequest toServiceRequest() {
        return InquiryQuestionSaveServiceRequest.builder()
                .title(title)
                .question(question)
                .build();
    }
}
