package com.addiction.faq.service;

import com.addiction.faq.service.response.FaqListResponse;

import java.util.List;

public interface FaqReadService {

    List<FaqListResponse> findAll();
}
