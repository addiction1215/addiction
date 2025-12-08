package com.addiction.mission.service.mission.request;

import com.addiction.common.enums.MissionCategoryStatus;
import com.addiction.common.enums.MissionStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MissionReportRequest {
    private Long missionId;

    private Long challengeId;

    private MissionStatus status;

	private MissionCategoryStatus categoryStatus;

    private Integer accTime;

    private String address;
}
