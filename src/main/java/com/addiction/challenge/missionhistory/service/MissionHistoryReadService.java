package com.addiction.challenge.missionhistory.service;

import com.addiction.challenge.missionhistory.entity.MissionHistory;
import com.addiction.challenge.missionhistory.service.response.MissionProgressResponse;

public interface MissionHistoryReadService {
    MissionProgressResponse getMissionProgress(Long challengeHistoryId);

    MissionHistory findById(Long missionHistoryId);
}
