package com.addiction.mission.service.mission.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MissionCompleteRequest {
    private Long missionHistoryId;
    
    // HOLD 타입: 누적 시간 필요
    private Long accTime;
    
    // REPLACE_ACTION 타입: 인증샷 필요
    // LOCATION 타입: 인증샷 + GPS 좌표 필요
    private String photoUrl;
    private Double latitude;
    private Double longitude;
}
