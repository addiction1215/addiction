package com.addiction.missionhistory.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MissionProgressResponse {
    private Long challengeHistoryId;
    private Long challengeId;
    private String challengeTitle;
    private Integer totalMissionCount;
    private Integer completedMissionCount;
    private Double completionRate; // 완료율 (%)
    private Integer totalEarnedReward; // 현재까지 획득한 총 포인트
    private Integer totalPossibleReward; // 모든 미션 완료시 획득 가능한 총 포인트
    private List<MissionItemResponse> missions;
}
