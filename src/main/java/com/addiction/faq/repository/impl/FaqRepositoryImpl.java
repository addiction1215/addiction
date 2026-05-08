package com.addiction.faq.repository.impl;

import com.addiction.faq.entity.Faq;
import com.addiction.faq.repository.FaqQueryRepository;
import com.addiction.faq.repository.FaqRepository;
import com.addiction.faq.service.request.FaqListServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FaqRepositoryImpl implements FaqRepository {

    private final FaqQueryRepository faqQueryRepository;

    @Override
    public Page<Faq> findAll(FaqListServiceRequest request) {
        return faqQueryRepository.findAll(request);
    }
}
