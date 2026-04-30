package com.addiction.challenge.missionhistory.service.response;

import lombok.Getter;

import java.util.List;

@Getter
public class MissionProgressingTitleResponse {
    private final List<String> titles;

    private MissionProgressingTitleResponse(List<String> titles) {
        this.titles = titles;
    }

    public static MissionProgressingTitleResponse createResponse(List<String> titles) {
        return new MissionProgressingTitleResponse(titles);
    }
}
