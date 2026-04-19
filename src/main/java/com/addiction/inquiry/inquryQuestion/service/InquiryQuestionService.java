package com.addiction.inquiry.inquryQuestion.service;

import com.addiction.inquiry.inquryQuestion.service.request.InquiryQuestionSaveServiceRequest;
import com.addiction.inquiry.inquryQuestion.service.request.InquiryQuestionUpdateServiceRequest;
import com.addiction.inquiry.inquryQuestion.service.response.InquiryQuestionSaveResponse;

public interface InquiryQuestionService {

    InquiryQuestionSaveResponse save(InquiryQuestionSaveServiceRequest inquiryQuestionSaveServiceRequest);

    void update(InquiryQuestionUpdateServiceRequest request);

    void delete(Long id);

}
