package com.addiction.missionhistory.controller;

import com.addiction.missionhistory.service.response.MissionProgressResponse;
import com.addiction.missionhistory.service.MissionHistoryReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mission-history")
@RequiredArgsConstructor
public class MissionHistoryController {
    private final MissionHistoryReadService missionHistoryReadService;

    @GetMapping("/progress/{challengeHistoryId}")
    public ResponseEntity<MissionProgressResponse> getMissionProgress(
            @PathVariable Long challengeHistoryId
    ) {
        return ResponseEntity.ok(missionHistoryReadService.getMissionProgress(challengeHistoryId));
    }
}
