package com.addiction.inquiry.inquryQuestion.repository;

import com.addiction.inquiry.inquryQuestion.entity.InquiryQuestion;
import com.addiction.inquiry.inquryQuestion.enums.InquiryStatus;

import java.util.List;

public interface InquiryQuestionRepository {

    InquiryQuestion save(InquiryQuestion inquiryQuestion);

    List<InquiryQuestion> findAllByUserIdAndInquiryStatus(int userId, InquiryStatus inquiryStatus);

}
