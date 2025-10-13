package com.addiction.challenge.challenge.controller;

import com.addiction.challenge.challenge.service.ChallengeReadService;
import com.addiction.challenge.challenge.service.challenge.response.ChallengeResponse;
import com.addiction.global.ApiResponse;
import com.addiction.global.page.request.PageInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/challenge")
public class ChallengeController {
    private final ChallengeReadService challengeService;

    @GetMapping("/list")
    public ApiResponse<ChallengeResponse> getChallengeList(@ModelAttribute PageInfoRequest request) {
        return ApiResponse.ok(challengeService.getChallenge(request.toServiceRequest()));
    }
}
