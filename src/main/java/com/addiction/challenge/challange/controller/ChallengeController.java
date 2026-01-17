package com.addiction.challenge.challange.controller;

import com.addiction.challenge.challange.service.ChallengeReadService;
import com.addiction.challenge.challange.service.response.ChallengeResponse;
import com.addiction.global.ApiResponse;
import com.addiction.global.page.request.PageInfoRequest;
import com.addiction.global.page.response.PageCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/challenge")
public class ChallengeController {
    private final ChallengeReadService challengeReadService;

    @GetMapping("/left")
    public ApiResponse<PageCustom<ChallengeResponse>> getLeftChallengeList(@ModelAttribute PageInfoRequest request) {
        return ApiResponse.ok(challengeReadService.getLeftChallengeList(request.toServiceRequest()));
    }
}
