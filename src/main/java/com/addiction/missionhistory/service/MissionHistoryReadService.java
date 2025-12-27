package com.addiction.missionhistory.service;

import com.addiction.missionhistory.service.response.MissionProgressResponse;

public interface MissionHistoryReadService {
    MissionProgressResponse getMissionProgress(Long challengeHistoryId);
}
