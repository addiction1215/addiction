package com.addiction.inquiry.inquryQuestion.service.impl;

import com.addiction.global.exception.NotFoundException;
import com.addiction.global.security.SecurityService;
import com.addiction.inquiry.inquryQuestion.entity.InquiryQuestion;
import com.addiction.inquiry.inquryQuestion.repository.InquiryQuestionRepository;
import com.addiction.inquiry.inquryQuestion.service.InquiryQuestionService;
import com.addiction.inquiry.inquryQuestion.service.request.InquiryQuestionSaveServiceRequest;
import com.addiction.inquiry.inquryQuestion.service.request.InquiryQuestionUpdateServiceRequest;
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

    @Override
    public void update(InquiryQuestionUpdateServiceRequest request) {
        InquiryQuestion inquiryQuestion = inquiryQuestionRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("해당 문의를 찾을 수 없습니다."));
        inquiryQuestion.updateQuestion(request.getQuestion());
    }

    @Override
    public void delete(Long id) {
        inquiryQuestionRepository.deleteById(id);
    }
}
