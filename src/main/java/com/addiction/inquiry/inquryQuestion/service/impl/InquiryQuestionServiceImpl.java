package com.addiction.inquiry.inquryQuestion.service.impl;

import com.addiction.global.security.SecurityService;
import com.addiction.inquiry.inquryQuestion.repository.InquiryQuestionRepository;
import com.addiction.inquiry.inquryQuestion.service.InquiryQuestionService;
import com.addiction.inquiry.inquryQuestion.service.request.InquiryQuestionSaveServiceRequest;
import com.addiction.inquiry.inquryQuestion.service.response.InquiryQuestionSaveResponse;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InquiryQuestionServiceImpl implements InquiryQuestionService {

    private final SecurityService securityService;
    private final UserReadService userReadService;

    private final InquiryQuestionRepository inquiryQuestionRepository;

    @Override
    public InquiryQuestionSaveResponse save(InquiryQuestionSaveServiceRequest inquiryQuestionSaveServiceRequest) {
        User user = userReadService.findById(securityService.getCurrentLoginUserInfo().getUserId());
        return InquiryQuestionSaveResponse.createResponse(inquiryQuestionRepository.save(inquiryQuestionSaveServiceRequest.toEntity(user)));
    }
}
