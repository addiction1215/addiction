package com.addiction.mission.service.mission.response;

import com.addiction.common.enums.MissionCategoryStatus;
import com.addiction.common.enums.MissionHistoryStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MissionHistoryResponse {
    private Long missionHistoryId;
    private Long missionId;
    private Long userId;
    private MissionCategoryStatus category;
    private MissionHistoryStatus status;
    
    // 시간 정보
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    // HOLD 타입: 누적 시간
    private Long accTime;
    
    // REPLACE_ACTION, LOCATION 타입: 인증샷
    private String photoUrl;
    
    // LOCATION 타입: GPS 좌표
    private Double latitude;
    private Double longitude;
    
    // 미션 기본 정보
    private String missionTitle;
    private String missionContent;
    private Integer reward;

    @Builder
    public MissionHistoryResponse(Long missionHistoryId, Long missionId, Long userId, 
                                 MissionCategoryStatus category, MissionHistoryStatus status,
                                 LocalDateTime startTime, LocalDateTime endTime, Long accTime,
                                 String photoUrl, Double latitude, Double longitude,
                                 String missionTitle, String missionContent, Integer reward) {
        this.missionHistoryId = missionHistoryId;
        this.missionId = missionId;
        this.userId = userId;
        this.category = category;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.accTime = accTime;
        this.photoUrl = photoUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.missionTitle = missionTitle;
        this.missionContent = missionContent;
        this.reward = reward;
    }
}
