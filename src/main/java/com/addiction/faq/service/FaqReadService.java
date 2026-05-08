package com.addiction.faq.service;

import com.addiction.faq.service.request.FaqListServiceRequest;
import com.addiction.faq.service.response.FaqListResponse;
import com.addiction.global.page.response.PageCustom;

public interface FaqReadService {

    PageCustom<FaqListResponse> findAll(FaqListServiceRequest request);
}
