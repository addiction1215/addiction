package com.addiction.inquiry.inquryQuestion.service.response;

import com.addiction.inquiry.inquiryAnswer.entity.InquiryAnswer;
import com.addiction.inquiry.inquryQuestion.entity.InquiryQuestion;
import com.addiction.inquiry.inquryQuestion.enums.InquiryStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Getter
public class InquiryQuestionDetailResponse {

    private final Long id;
    private final String title;
    private final String question;
    private final String answer;
    private final List<String> imageKeys;
    private final InquiryStatus inquiryStatus;
    private final LocalDateTime createdDate;

    @Builder
    public InquiryQuestionDetailResponse(Long id, String title, String question, String answer, List<String> imageKeys,
                                         InquiryStatus inquiryStatus, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.question = question;
        this.answer = answer;
        this.imageKeys = imageKeys;
        this.inquiryStatus = inquiryStatus;
        this.createdDate = createdDate;
    }

    public static InquiryQuestionDetailResponse createResponse(InquiryQuestion inquiryQuestion) {
        List<String> imageKeys = inquiryQuestion.getImageKeys();
        String answer = resolveLatestAnswer(inquiryQuestion.getInquiryAnswers());

        return InquiryQuestionDetailResponse.builder()
                .id(inquiryQuestion.getId())
                .title(inquiryQuestion.getTitle())
                .question(inquiryQuestion.getQuestion())
                .answer(answer)
                .imageKeys(imageKeys == null ? null : List.copyOf(imageKeys))
                .inquiryStatus(inquiryQuestion.getInquiryStatus())
                .createdDate(inquiryQuestion.getCreatedDate())
                .build();
    }

    private static String resolveLatestAnswer(List<InquiryAnswer> inquiryAnswers) {
        if (inquiryAnswers == null || inquiryAnswers.isEmpty()) {
            return null;
        }

        return inquiryAnswers.stream()
                .max(Comparator.comparing(InquiryAnswer::getCreatedDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(InquiryAnswer::getContent)
                .orElse(null);
    }
}
