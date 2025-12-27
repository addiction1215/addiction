package com.addiction.missionhistory.dto.response;

import com.addiction.common.enums.MissionCategoryStatus;
import com.addiction.common.enums.MissionStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MissionItemResponse {
    private Long missionId;
    private String title;
    private String content;
    private MissionCategoryStatus category;
    private Integer reward;
    private MissionStatus status; // COMPLETED, PROGRESSING 등
    private Long missionHistoryId; // 완료된 경우에만 값이 있음
}
