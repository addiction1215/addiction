package com.addiction.challenge.missionhistory.service.response;

import lombok.Builder;
import lombok.Getter;


@Getter
public class MissionSubmitResponse {
    private final Long missionHistoryId;

    @Builder
    public MissionSubmitResponse(Long missionHistoryId) {
        this.missionHistoryId = missionHistoryId;
    }

    public static MissionSubmitResponse createResponse(Long missionHistoryId) {
        return MissionSubmitResponse.builder()
                .missionHistoryId(missionHistoryId)
                .build();
    }
}
