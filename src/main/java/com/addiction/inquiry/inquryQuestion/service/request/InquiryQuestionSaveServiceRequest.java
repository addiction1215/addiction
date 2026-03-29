package com.addiction.inquiry.inquryQuestion.service.request;

import com.addiction.inquiry.inquryQuestion.entity.InquiryQuestion;
import com.addiction.inquiry.inquryQuestion.enums.InquiryStatus;
import com.addiction.user.users.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class InquiryQuestionSaveServiceRequest {

    private final String title;
    private final String question;
    private final List<String> imageKeys;

    @Builder
    public InquiryQuestionSaveServiceRequest(String question, String title, List<String> imageKeys) {
        this.question = question;
        this.title = title;
        this.imageKeys = imageKeys;
    }

    public InquiryQuestion toEntity(User user) {
        return InquiryQuestion.builder()
                .title(title)
                .question(question)
                .user(user)
                .inquiryStatus(InquiryStatus.WAITING)
                .imageKeys(imageKeys)
                .build();
    }
}
