package com.addiction.challenge.mission.service.response;

import com.addiction.challenge.mission.entity.Mission;
import com.addiction.challenge.mission.entity.MissionCategoryStatus;
import com.addiction.challenge.missionhistory.entity.MissionStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MissionResponse {
    private final Long missionId;
    private final String title;
    private final String content;
    private final Integer reward;
    private final MissionCategoryStatus category;
    private final MissionStatus status;

    @Builder
    public MissionResponse(Long missionId, String title, String content, Integer reward,
                           MissionCategoryStatus category, MissionStatus status) {
        this.missionId = missionId;
        this.title = title;
        this.content = content;
        this.reward = reward;
        this.category = category;
        this.status = status;
    }


    public static MissionResponse createResponse(Mission mission) {
        return MissionResponse.builder()
                .missionId(mission.getId())
                .title(mission.getTitle())
                .content(mission.getContent())
                .reward(mission.getReward())
                .category(mission.getCategory())
                .build();
    }
}
