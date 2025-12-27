package com.addiction.mission.service;

import com.addiction.mission.service.mission.request.MissionReportRequest;

public interface MissionService {
    boolean insertMissionReport(MissionReportRequest request);
}
