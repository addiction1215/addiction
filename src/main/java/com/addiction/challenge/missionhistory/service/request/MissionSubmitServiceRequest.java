package com.addiction.challenge.missionhistory.service.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MissionSubmitServiceRequest {
    private final Long missionHistoryId;
    private final String address;
    private final String photoUrl;
    private final Integer photoNumber;
    private final Integer time;

    @Builder
    public MissionSubmitServiceRequest(Long missionHistoryId, String address, String photoUrl, Integer photoNumber, Integer time) {
        this.missionHistoryId = missionHistoryId;
        this.address = address;
        this.photoUrl = photoUrl;
        this.photoNumber = photoNumber;
        this.time = time;
    }
}
