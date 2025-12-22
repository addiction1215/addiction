package com.addiction.challenge.controller;

import com.addiction.challenge.service.ChallengeReadService;
import com.addiction.challenge.service.ChallengeService;
import com.addiction.challenge.service.challenge.request.FailChallengeRequest;
import com.addiction.challenge.service.challenge.response.ChallengeResponse;
import com.addiction.global.ApiResponse;
import com.addiction.global.page.request.PageInfoRequest;
import com.addiction.global.page.response.PageCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/challenge")
public class ChallengeController {
    private final ChallengeReadService challengeReadService;
    private final ChallengeService challengeService;

    @GetMapping("/progressing")
    public ApiResponse<ChallengeResponse> getProgressingChallenge() {
        return ApiResponse.ok(challengeReadService.getProgressingChallenge());
    }

    @GetMapping("/left")
    public ApiResponse<PageCustom<ChallengeResponse>> getLeftChallengeList(@ModelAttribute PageInfoRequest request) {
        return ApiResponse.ok(challengeReadService.getLeftChallengeList(request.toServiceRequest()));
    }

    @GetMapping("/finished")
    public ApiResponse<PageCustom<ChallengeResponse>> getFinishedChallengeList(@ModelAttribute PageInfoRequest request) {
        return ApiResponse.ok(challengeReadService.getFinishedChallengeList(request.toServiceRequest()));
    }

    @PutMapping("/fail")
    public ApiResponse<String> updateFailChallenge(@RequestBody FailChallengeRequest request) {
        challengeService.updateFailChallenge(request);
        return ApiResponse.ok("챌린지를 포기했습니다.");
    }

}
