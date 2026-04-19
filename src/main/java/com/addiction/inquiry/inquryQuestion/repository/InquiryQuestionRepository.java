package com.addiction.inquiry.inquryQuestion.repository;

import com.addiction.inquiry.inquryQuestion.entity.InquiryQuestion;
import com.addiction.inquiry.inquryQuestion.enums.InquiryStatus;

import java.util.List;
import java.util.Optional;

public interface InquiryQuestionRepository {

    InquiryQuestion save(InquiryQuestion inquiryQuestion);

    List<InquiryQuestion> findAllByUserIdAndInquiryStatus(Long userId, InquiryStatus inquiryStatus);

    Optional<InquiryQuestion> findById(Long id);

    void deleteById(Long id);

}
