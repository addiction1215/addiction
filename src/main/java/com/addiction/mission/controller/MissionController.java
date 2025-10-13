package com.addiction.mission.controller;

import com.addiction.mission.service.MissionReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mission")
public class MissionController {
    private final MissionReadService missionReadService;

    
}
