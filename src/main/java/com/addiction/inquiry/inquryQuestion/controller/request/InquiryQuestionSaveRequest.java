package com.addiction.inquiry.inquryQuestion.controller.request;

import com.addiction.inquiry.inquryQuestion.service.request.InquiryQuestionSaveServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class InquiryQuestionSaveRequest {

    @NotNull(message = "제목은 필수입니다.")
    private String title;
    @NotNull(message = "질문 내용은 필수입니다.")
    private String question;
    private List<String> imageKeys;

    @Builder
    public InquiryQuestionSaveRequest(String question, String title, List<String> imageKeys) {
        this.question = question;
        this.title = title;
        this.imageKeys = imageKeys;
    }

    public InquiryQuestionSaveServiceRequest toServiceRequest() {
        return InquiryQuestionSaveServiceRequest.builder()
                .title(title)
                .question(question)
                .imageKeys(imageKeys)
                .build();
    }
}
