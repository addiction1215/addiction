package com.addiction.inquiry.inquryQuestion.repository.impl;

import com.addiction.inquiry.inquryQuestion.entity.InquiryQuestion;
import com.addiction.inquiry.inquryQuestion.enums.InquiryStatus;
import com.addiction.inquiry.inquryQuestion.repository.InquiryQuestionJpaRepository;
import com.addiction.inquiry.inquryQuestion.repository.InquiryQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class InquiryQuestionRepositoryImpl implements InquiryQuestionRepository {

    private final InquiryQuestionJpaRepository inquiryQuestionJpaRepository;

    @Override
    public InquiryQuestion save(InquiryQuestion inquiryQuestion) {
        return inquiryQuestionJpaRepository.save(inquiryQuestion);
    }

    @Override
    public List<InquiryQuestion> findAllByUserIdAndInquiryStatus(Long userId, InquiryStatus inquiryStatus) {
        return inquiryQuestionJpaRepository.findAllByUserIdAndInquiryStatus(userId, inquiryStatus);
    }
}
