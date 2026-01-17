package com.addiction.challenge.missionhistory.controller.request;

import com.addiction.challenge.missionhistory.service.request.MissionSubmitServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MissionSubmitRequest {

    private Long missionHistoryId;
    private String address;
    private String photoUrl;
    private Integer time;

    @Builder
    public MissionSubmitRequest(Long missionHistoryId, String address, String photoUrl, Integer time) {
        this.missionHistoryId = missionHistoryId;
        this.address = address;
        this.photoUrl = photoUrl;
        this.time = time;
    }

    public MissionSubmitServiceRequest toServiceRequest() {
        return MissionSubmitServiceRequest.builder()
                .missionHistoryId(missionHistoryId)
                .address(address)
                .photoUrl(photoUrl)
                .time(time)
                .build();
    }
}
