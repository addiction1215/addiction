package com.addiction.faq.controller;

import com.addiction.faq.service.FaqReadService;
import com.addiction.faq.service.response.FaqListResponse;
import com.addiction.global.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/faq")
public class FaqController {

    private final FaqReadService faqReadService;

    @GetMapping
    public ApiResponse<List<FaqListResponse>> findAll() {
        return ApiResponse.ok(faqReadService.findAll());
    }
}
