package com.addiction.challenge.missionhistory.service.response;

import com.addiction.challenge.mission.entity.MissionCategoryStatus;
import com.addiction.challenge.missionhistory.entity.MissionStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MissionItemResponse {
    private Long missionHistoryId;
    private Long missionId;
    private String missionTitle;
    private String missionContent;
    private MissionCategoryStatus category;
    private Integer reward;
    private MissionStatus status;
    private String address;
    private LocalDateTime completeAt;
}
