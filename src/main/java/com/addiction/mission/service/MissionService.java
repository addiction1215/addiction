package com.addiction.mission.service;

import com.addiction.mission.service.mission.request.MissionCompleteRequest;
import com.addiction.mission.service.mission.request.MissionReportRequest;
import com.addiction.mission.service.mission.request.MissionStartRequest;
import com.addiction.mission.service.mission.response.MissionDetailResponse;
import com.addiction.mission.service.mission.response.MissionHistoryResponse;

public interface MissionService {
    boolean insertMissionReport(MissionReportRequest request);
    
    /**
     * 미션 상세 조회
     */
    MissionDetailResponse getMissionDetail(Long missionId);
    
    /**
     * 미션 시작
     */
    MissionHistoryResponse startMission(MissionStartRequest request);
    
    /**
     * 미션 완료
     */
    MissionHistoryResponse completeMission(MissionCompleteRequest request);
    
    /**
     * 진행 중인 미션 조회
     */
    MissionHistoryResponse getProgressingMission();
}
