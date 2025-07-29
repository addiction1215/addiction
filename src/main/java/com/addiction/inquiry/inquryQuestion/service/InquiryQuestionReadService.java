package com.addiction.inquiry.inquryQuestion.service;

import com.addiction.inquiry.inquryQuestion.enums.InquiryStatus;
import com.addiction.inquiry.inquryQuestion.service.response.InquiryQuestionFindResponse;

import java.util.List;

public interface InquiryQuestionReadService {

    List<InquiryQuestionFindResponse> findAllByUserIdAndInquiryStatus(InquiryStatus inquiryStatus);

}
