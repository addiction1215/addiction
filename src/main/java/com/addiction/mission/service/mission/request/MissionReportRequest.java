package com.addiction.mission.service.mission.request;

import com.addiction.common.enums.ChallengeStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MissionReportRequest {
    private Long missionId;

    private Long challengeId;

    private ChallengeStatus status;

    private Integer accTime;

    private String address;
}
