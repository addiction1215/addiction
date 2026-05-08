package com.addiction.faq.service.impl;

import com.addiction.faq.repository.FaqRepository;
import com.addiction.faq.service.FaqReadService;
import com.addiction.faq.service.request.FaqListServiceRequest;
import com.addiction.faq.service.response.FaqListResponse;
import com.addiction.global.page.response.PageCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FaqReadServiceImpl implements FaqReadService {

    private final FaqRepository faqRepository;

    @Override
    public PageCustom<FaqListResponse> findAll(FaqListServiceRequest request) {
        Page<FaqListResponse> page = faqRepository.findAll(request)
                .map(FaqListResponse::createResponse);
        return PageCustom.of(page);
    }
}
