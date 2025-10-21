package com.addiction.mission.service;

import com.addiction.mission.service.mission.request.MissionReportRequest;
import com.addiction.mission.service.mission.response.MissionResponseList;

import java.util.List;

public interface MissionReadService {
    List<MissionResponseList> getMission(Long challengeId);
}
