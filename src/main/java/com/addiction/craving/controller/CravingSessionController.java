package com.addiction.craving.controller;

import com.addiction.craving.service.CravingSessionService;
import com.addiction.craving.service.response.CravingSessionCompleteResponse;
import com.addiction.craving.service.response.CravingSessionResponse;
import com.addiction.craving.service.response.TodayCravingSummaryResponse;
import com.addiction.global.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/craving-sessions")
public class CravingSessionController {
    private final CravingSessionService cravingSessionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CravingSessionResponse> start() {
        return ApiResponse.created(cravingSessionService.start());
    }

    @PostMapping("/{sessionId}/complete")
    public ApiResponse<CravingSessionCompleteResponse> complete(
            @PathVariable Long sessionId
    ) {
        return ApiResponse.ok(cravingSessionService.complete(sessionId));
    }

    @GetMapping("/today")
    public ApiResponse<TodayCravingSummaryResponse> getTodaySummary() {
        return ApiResponse.ok(cravingSessionService.getTodaySummary());
    }
}
