package com.addiction.challenge.controller;

import com.addiction.challenge.service.ChallengeReadService;
import com.addiction.challenge.service.ChallengeService;
import com.addiction.challenge.service.challenge.response.ChallengeResponse;
import com.addiction.global.ApiResponse;
import com.addiction.global.page.request.PageInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/challenge")
public class ChallengeController {
    private final ChallengeReadService challengeReadService;
    private final ChallengeService challengeService;

    @GetMapping("/list")
    public ApiResponse<ChallengeResponse> getChallengeList(@ModelAttribute PageInfoRequest request) {
        return ApiResponse.ok(challengeReadService.getChallenge(request.toServiceRequest()));
    }
}
