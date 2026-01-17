package com.addiction.challenge.missionhistory.controller;

import com.addiction.challenge.missionhistory.controller.request.MissionSubmitRequest;
import com.addiction.challenge.missionhistory.service.response.MissionDetailResponse;
import com.addiction.challenge.missionhistory.service.response.MissionProgressResponse;
import com.addiction.challenge.missionhistory.service.MissionHistoryReadService;
import com.addiction.challenge.missionhistory.service.MissionHistoryService;
import com.addiction.challenge.missionhistory.service.response.MissionSubmitResponse;
import com.addiction.global.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mission-history")
@RequiredArgsConstructor
public class MissionHistoryController {
    private final MissionHistoryReadService missionHistoryReadService;
    private final MissionHistoryService missionHistoryService;

    @GetMapping("/progress/{challengeHistoryId}")
    public ApiResponse<MissionProgressResponse> getMissionProgress(
            @PathVariable Long challengeHistoryId
    ) {
        return ApiResponse.ok(missionHistoryReadService.getMissionProgress(challengeHistoryId));
    }

    /**
     * 미션 상세 조회
     */
    @GetMapping("/detail/{missionHistoryId}")
    public ApiResponse<MissionDetailResponse> getMissionDetail(
            @PathVariable Long missionHistoryId
    ) {
        return ApiResponse.ok(missionHistoryService.getMissionDetail(missionHistoryId));
    }

    /**
     * 미션 중간 제출
     */
    @PostMapping("/submit")
    public ApiResponse<MissionSubmitResponse> submitMission(
            @Valid @RequestBody MissionSubmitRequest request
    ) {
        return ApiResponse.ok(missionHistoryService.submitMission(request.toServiceRequest()));
    }

    /**
     * 미션 최종 제출
     */
    @PatchMapping("/complete/{missionHistoryId}")
    public ApiResponse<MissionSubmitResponse> completeMission(
            @PathVariable Long missionHistoryId
    ) {
        return ApiResponse.ok(missionHistoryService.completeMission(missionHistoryId));
    }
}
