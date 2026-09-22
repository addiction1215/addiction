package com.addiction.craving.service;

import com.addiction.craving.service.response.CravingSessionCompleteResponse;
import com.addiction.craving.service.response.CravingSessionResponse;
import com.addiction.craving.service.response.TodayCravingSummaryResponse;

public interface CravingSessionService {
    CravingSessionResponse start();

    CravingSessionCompleteResponse complete(Long sessionId);

    TodayCravingSummaryResponse getTodaySummary();
}
