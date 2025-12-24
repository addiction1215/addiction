package com.addiction.mission.service.mission.response;

import com.addiction.common.enums.MissionCategoryStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MissionDetailResponse {
    private Long missionId;
    private Long challengeId;
    private MissionCategoryStatus category;
    private String title;
    private Integer reward;
    private String content;
    
    // 타입별 요구사항
    private String requirements;
    
    // HOLD 타입: 필요 시간
    private Long requiredTime;
    
    // LOCATION 타입: 목표 위치
    private Double targetLatitude;
    private Double targetLongitude;
    private String locationName;

    @Builder
    public MissionDetailResponse(Long missionId, Long challengeId, MissionCategoryStatus category, 
                                String title, Integer reward, String content, String requirements,
                                Long requiredTime, Double targetLatitude, Double targetLongitude, 
                                String locationName) {
        this.missionId = missionId;
        this.challengeId = challengeId;
        this.category = category;
        this.title = title;
        this.reward = reward;
        this.content = content;
        this.requirements = requirements;
        this.requiredTime = requiredTime;
        this.targetLatitude = targetLatitude;
        this.targetLongitude = targetLongitude;
        this.locationName = locationName;
    }
}
