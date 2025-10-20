package com.addiction.mission.controller;

import com.addiction.global.ApiResponse;
import com.addiction.mission.service.MissionReadService;
import com.addiction.mission.service.mission.response.MissionResponseList;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mission")
public class MissionController {
    private final MissionReadService missionReadService;

    @GetMapping("list")
    public ApiResponse<List<MissionResponseList>> getMissionList(@RequestParam Long challengeId) {
        return ApiResponse.ok(missionReadService.getMission(challengeId));
    }
}
