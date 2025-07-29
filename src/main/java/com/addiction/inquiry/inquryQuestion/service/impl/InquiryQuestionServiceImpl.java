package com.addiction.inquiry.inquryQuestion.service.impl;

import com.addiction.inquiry.inquryQuestion.repository.InquiryQuestionRepository;
import com.addiction.inquiry.inquryQuestion.service.InquiryQuestionService;
import com.addiction.inquiry.inquryQuestion.service.request.InquiryQuestionSaveServiceRequest;
import com.addiction.inquiry.inquryQuestion.service.response.InquiryQuestionSaveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InquiryQuestionServiceImpl implements InquiryQuestionService {

    private final InquiryQuestionRepository inquiryQuestionRepository;

    @Override
    public InquiryQuestionSaveResponse save(InquiryQuestionSaveServiceRequest inquiryQuestionSaveServiceRequest) {
        return InquiryQuestionSaveResponse.createResponse(inquiryQuestionRepository.save(inquiryQuestionSaveServiceRequest.toEntity()));
    }
}
