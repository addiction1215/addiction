package com.addiction.inquiry.inquryQuestion.service.impl;

import com.addiction.global.exception.NotFoundException;
import com.addiction.global.security.SecurityService;
import com.addiction.inquiry.inquryQuestion.enums.InquiryStatus;
import com.addiction.inquiry.inquryQuestion.repository.InquiryQuestionRepository;
import com.addiction.inquiry.inquryQuestion.service.InquiryQuestionReadService;
import com.addiction.inquiry.inquryQuestion.service.response.InquiryQuestionDetailResponse;
import com.addiction.inquiry.inquryQuestion.service.response.InquiryQuestionFindResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryQuestionReadServiceImpl implements InquiryQuestionReadService {

    private final SecurityService securityService;

    private final InquiryQuestionRepository inquiryQuestionRepository;

    @Override
    public List<InquiryQuestionFindResponse> findAllByUserIdAndInquiryStatus(InquiryStatus inquiryStatus) {
        long userId = securityService.getCurrentLoginUserInfo().getUserId();
        return inquiryQuestionRepository.findAllByUserIdAndInquiryStatus(userId, inquiryStatus)
                .stream()
                .map(InquiryQuestionFindResponse::createResponse)
                .toList();
    }

    @Override
    public InquiryQuestionDetailResponse findById(Long id) {
        return inquiryQuestionRepository.findById(id)
                .map(InquiryQuestionDetailResponse::createResponse)
                .orElseThrow(() -> new NotFoundException("해당 문의를 찾을 수 없습니다."));
    }

}
