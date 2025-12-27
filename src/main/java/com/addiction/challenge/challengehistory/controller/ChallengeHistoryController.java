package com.addiction.challenge.challengehistory.controller;

import com.addiction.challenge.challengehistory.service.ChallengeHistoryReadService;
import com.addiction.challenge.challengehistory.service.response.ChallengeHistoryResponse;
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
@RequestMapping("/api/v1/challenge-history")
public class ChallengeHistoryController {
    private final ChallengeHistoryReadService challengeHistoryReadService;

    @GetMapping("/progressing")
    public ApiResponse<ChallengeHistoryResponse> getProgressingChallenge() {
        return ApiResponse.ok(challengeHistoryReadService.getProgressingChallenge());
    }

    @GetMapping("/finished")
    public ApiResponse<PageCustom<ChallengeHistoryResponse>> getFinishedChallengeList(@ModelAttribute PageInfoRequest request) {
        return ApiResponse.ok(challengeHistoryReadService.getFinishedChallengeList(request.toServiceRequest()));
    }

}
