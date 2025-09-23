package com.addiction.challenge.controller;

import com.addiction.challenge.service.ChallengeReadService;
import com.addiction.challenge.service.challenge.response.ChallengeResponse;
import com.addiction.common.enums.YnStatus;
import com.addiction.global.ApiResponse;
import com.addiction.global.page.request.PageInfoRequest;
import com.addiction.global.page.response.PageCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/challenge")
public class ChallengeController {
    private final ChallengeReadService challengeService;

    @GetMapping("/{finishYn}")
    public ApiResponse<PageCustom<ChallengeResponse>> getChallengeList(@PathVariable("finishYn") YnStatus finishYn, @ModelAttribute PageInfoRequest request) {
        return ApiResponse.ok(challengeService.getChallenge(finishYn, request.toServiceRequest()));
    }
}
