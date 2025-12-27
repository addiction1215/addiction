package com.addiction.challenge.mission.controller;

import com.addiction.global.ApiResponse;
import com.addiction.challenge.mission.service.MissionReadService;
import com.addiction.challenge.mission.service.response.MissionListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mission")
public class MissionController {

    private final MissionReadService missionReadService;

    @GetMapping("/challenge/{challengeId}")
    public ApiResponse<MissionListResponse> getMissionListByChallengeId(
            @PathVariable Long challengeId) {
        return ApiResponse.ok(missionReadService.getMissionListByChallengeId(challengeId));
    }
}
