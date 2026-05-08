package com.addiction.faq.controller;

import com.addiction.faq.controller.request.FaqListRequest;
import com.addiction.faq.service.FaqReadService;
import com.addiction.faq.service.response.FaqListResponse;
import com.addiction.global.ApiResponse;
import com.addiction.global.page.response.PageCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/faq")
public class FaqController {

    private final FaqReadService faqReadService;

    @GetMapping
    public ApiResponse<PageCustom<FaqListResponse>> findAll(@ModelAttribute FaqListRequest request) {
        return ApiResponse.ok(faqReadService.findAll(request.toServiceRequest()));
    }
}
