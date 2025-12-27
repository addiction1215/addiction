package com.addiction.challenge.missionhistory.service;

import com.addiction.challenge.missionhistory.service.response.MissionProgressResponse;

public interface MissionHistoryReadService {
    MissionProgressResponse getMissionProgress(Long challengeHistoryId);
}
