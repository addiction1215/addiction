package com.addiction.inquiry.inquryQuestion.service;

import com.addiction.inquiry.inquryQuestion.service.request.InquiryQuestionSaveServiceRequest;
import com.addiction.inquiry.inquryQuestion.service.response.InquiryQuestionSaveResponse;

public interface InquiryQuestionService {

    InquiryQuestionSaveResponse save(InquiryQuestionSaveServiceRequest inquiryQuestionSaveServiceRequest);

}
