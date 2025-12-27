package com.addiction.mission.controller;

import com.addiction.global.ApiResponse;
import com.addiction.mission.service.MissionReadService;
import com.addiction.mission.service.MissionService;
import com.addiction.mission.service.mission.request.MissionReportRequest;
import com.addiction.mission.service.mission.response.MissionResponseList;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mission")
public class MissionController {
    private final MissionReadService missionReadService;
    private final MissionService missionService;

    @GetMapping("/list")
    public ApiResponse<List<MissionResponseList>> getMissionList(@RequestParam Long challengeId) {
        return ApiResponse.ok(missionReadService.getMission(challengeId));
    }

    @PostMapping("/report")
    public ApiResponse<Boolean> insertMissionReport(@RequestBody MissionReportRequest request) {
        return ApiResponse.ok(missionService.insertMissionReport(request));
    }
}
