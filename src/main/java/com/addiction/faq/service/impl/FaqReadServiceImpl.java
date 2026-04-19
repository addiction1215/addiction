package com.addiction.faq.service.impl;

import com.addiction.faq.repository.FaqRepository;
import com.addiction.faq.service.FaqReadService;
import com.addiction.faq.service.response.FaqListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FaqReadServiceImpl implements FaqReadService {

    private final FaqRepository faqRepository;

    @Override
    public List<FaqListResponse> findAll() {
        return faqRepository.findAll()
                .stream()
                .map(FaqListResponse::createResponse)
                .toList();
    }
}
