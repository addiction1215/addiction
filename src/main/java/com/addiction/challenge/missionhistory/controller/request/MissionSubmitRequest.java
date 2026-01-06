package com.addiction.challenge.missionhistory.controller.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionSubmitRequest {

    private Long missionHistoryId;
    private String address;
    private String photoUrl;
    private Integer photoNumber;
    private Integer time;

    @Builder
    public MissionSubmitRequest(Long missionHistoryId, String address, String photoUrl, Integer photoNumber, Integer time) {
        this.missionHistoryId = missionHistoryId;
        this.address = address;
        this.photoUrl = photoUrl;
        this.photoNumber = photoNumber;
        this.time = time;
    }
}
