package com.addiction.faq.repository;

import com.addiction.faq.entity.Faq;
import com.addiction.faq.service.request.FaqListServiceRequest;
import org.springframework.data.domain.Page;

public interface FaqRepository {

    Page<Faq> findAll(FaqListServiceRequest request);
}
