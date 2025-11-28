package com.addiction.mission.service.mission.response;

import com.addiction.common.enums.MissionCategoryStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MissionResponseList {
    private Long missionId;
    private Long challengeId;
    private MissionCategoryStatus category;
    private String title;
    private Integer reward;
    private String content;

    @Builder
    public MissionResponseList(Long missionId, Long challengeId, MissionCategoryStatus category, String title, Integer reward, String content) {
        this.missionId = missionId;
        this.challengeId = challengeId;
        this.category = category;
        this.title = title;
        this.reward = reward;
        this.content = content;
    }
}
