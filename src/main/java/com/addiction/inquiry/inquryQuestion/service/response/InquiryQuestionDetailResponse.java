package com.addiction.inquiry.inquryQuestion.service.response;

import com.addiction.inquiry.inquryQuestion.entity.InquiryQuestion;
import com.addiction.inquiry.inquryQuestion.enums.InquiryStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class InquiryQuestionDetailResponse {

    private final Long id;
    private final String title;
    private final String question;
    private final List<String> imageKeys;
    private final InquiryStatus inquiryStatus;
    private final LocalDateTime createdDate;

    @Builder
    public InquiryQuestionDetailResponse(Long id, String title, String question, List<String> imageKeys,
                                         InquiryStatus inquiryStatus, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.question = question;
        this.imageKeys = imageKeys;
        this.inquiryStatus = inquiryStatus;
        this.createdDate = createdDate;
    }

    public static InquiryQuestionDetailResponse createResponse(InquiryQuestion inquiryQuestion) {
        return InquiryQuestionDetailResponse.builder()
                .id(inquiryQuestion.getId())
                .title(inquiryQuestion.getTitle())
                .question(inquiryQuestion.getQuestion())
                .imageKeys(inquiryQuestion.getImageKeys())
                .inquiryStatus(inquiryQuestion.getInquiryStatus())
                .createdDate(inquiryQuestion.getCreatedDate())
                .build();
    }
}
