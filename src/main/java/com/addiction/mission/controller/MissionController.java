package com.addiction.mission.controller;

import com.addiction.global.ApiResponse;
import com.addiction.mission.service.MissionReadService;
import com.addiction.mission.service.MissionService;
import com.addiction.mission.service.mission.request.MissionCompleteRequest;
import com.addiction.mission.service.mission.request.MissionReportRequest;
import com.addiction.mission.service.mission.request.MissionStartRequest;
import com.addiction.mission.service.mission.response.MissionDetailResponse;
import com.addiction.mission.service.mission.response.MissionHistoryResponse;
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
	
    /**
     * 챌린지별 미션 목록 조회
     */
    @GetMapping("/list")
    public ApiResponse<List<MissionResponseList>> getMissionList(@RequestParam Long challengeId) {
        return ApiResponse.ok(missionReadService.getMission(challengeId));
    }

    @PostMapping("report")
    public ApiResponse<String> insertMissionReport(@RequestBody MissionReportRequest request) {
        missionService.insertMissionReport(request);
        return ApiResponse.ok("미션 결과가 제출되었습니다.");
    /**
     * 미션 상세 조회
     * - 미션의 타입(HOLD/REPLACE_ACTION/LOCATION)에 따른 요구사항 포함
     */
    @GetMapping("/{missionId}/detail")
    public ApiResponse<MissionDetailResponse> getMissionDetail(@PathVariable Long missionId) {
        return ApiResponse.ok(missionService.getMissionDetail(missionId));
    }

    /**
     * 미션 시작
     * - MissionHistory 생성 (상태: PROGRESSING)
     */
    @PostMapping("/start")
    public ApiResponse<MissionHistoryResponse> startMission(@RequestBody MissionStartRequest request) {
        return ApiResponse.ok(missionService.startMission(request));
    }

    /**
     * 미션 완료
     * - HOLD: accTime (누적 시간) 필요
     * - REPLACE_ACTION: photoUrl (인증샷) 필요
     * - LOCATION: photoUrl + latitude, longitude (GPS + 인증샷) 필요
     */
    @PutMapping("/complete")
    public ApiResponse<MissionHistoryResponse> completeMission(@RequestBody MissionCompleteRequest request) {
        return ApiResponse.ok(missionService.completeMission(request));
    }

    /**
     * 진행 중인 미션 조회
     */
    @GetMapping("/progressing")
    public ApiResponse<MissionHistoryResponse> getProgressingMission() {
        return ApiResponse.ok(missionService.getProgressingMission());
    }

    /**
     * 미션 신고 (기존 API)
     */
    @PostMapping("/report")
    public ApiResponse<Boolean> insertMissionReport(@RequestBody MissionReportRequest request) {
        return ApiResponse.ok(missionService.insertMissionReport(request));
    }
}
