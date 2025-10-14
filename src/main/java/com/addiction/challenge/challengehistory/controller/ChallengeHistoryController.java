package com.addiction.challenge.challengehistory.controller;

import com.addiction.challenge.challengehistory.service.ChallengeHistoryReadService;
import com.addiction.challenge.challengehistory.service.response.ChallengeHistoryUserResponse;
import com.addiction.global.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/challeng-history")
public class ChallengeHistoryController {

    private final ChallengeHistoryReadService challengeHistoryReadService;

    @GetMapping
    public ApiResponse<List<ChallengeHistoryUserResponse>> findByUserId() {
        return ApiResponse.ok(challengeHistoryReadService.findByUserId());
    }

}
