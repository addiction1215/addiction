package com.addiction.challenge.challengehistory.controller;

import com.addiction.challenge.challengehistory.controller.request.ChallengeCancelRequest;
import com.addiction.challenge.challengehistory.controller.request.ChallengeJoinRequest;
import com.addiction.challenge.challengehistory.service.ChallengeHistoryReadService;
import com.addiction.challenge.challengehistory.service.ChallengeHistoryService;
import com.addiction.challenge.challengehistory.service.response.ChallengeCancelResponse;
import com.addiction.challenge.challengehistory.service.response.ChallengeHistoryResponse;
import com.addiction.challenge.challengehistory.service.response.ChallengeJoinResponse;
import com.addiction.global.ApiResponse;
import com.addiction.global.page.request.PageInfoRequest;
import com.addiction.global.page.response.PageCustom;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/challenge-history")
public class ChallengeHistoryController {
    private final ChallengeHistoryReadService challengeHistoryReadService;
    private final ChallengeHistoryService challengeHistoryService;

    @GetMapping("/progressing")
    public ApiResponse<ChallengeHistoryResponse> getProgressingChallenge() {
        return ApiResponse.ok(challengeHistoryReadService.getProgressingChallenge());
    }

    @GetMapping("/finished")
    public ApiResponse<PageCustom<ChallengeHistoryResponse>> getFinishedChallengeList(@ModelAttribute PageInfoRequest request) {
        return ApiResponse.ok(challengeHistoryReadService.getFinishedChallengeList(request.toServiceRequest()));
    }

    @PostMapping("/join")
    public ApiResponse<ChallengeJoinResponse> joinChallenge(@Valid @RequestBody ChallengeJoinRequest request) {
        return ApiResponse.ok(challengeHistoryService.joinChallenge(request));
    }

    @PatchMapping("/cancel")
    public ApiResponse<ChallengeCancelResponse> cancelChallenge(@Valid @RequestBody ChallengeCancelRequest request) {
        return ApiResponse.ok(challengeHistoryService.cancelChallenge(request));
    }
}
