package com.addiction.challenge.missionhistory.service.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MissionProgressResponse {
    private final Long challengeHistoryId;
    private final Long challengeId;
    private final String challengeTitle;
    private final Integer totalMissionCount;
    private final Integer completedMissionCount;
    private final Integer totalEarnedReward; // 현재까지 획득한 총 포인트
    private final Integer totalPossibleReward; // 모든 미션 완료시 획득 가능한 총 포인트
    private final List<MissionHistoryResponse> missions;

    @Builder
    public MissionProgressResponse(Long challengeHistoryId, Long challengeId, String challengeTitle, Integer totalMissionCount,
                                   Integer completedMissionCount, Integer totalEarnedReward,
                                   Integer totalPossibleReward, List<MissionHistoryResponse> missions) {
        this.challengeHistoryId = challengeHistoryId;
        this.challengeId = challengeId;
        this.challengeTitle = challengeTitle;
        this.totalMissionCount = totalMissionCount;
        this.completedMissionCount = completedMissionCount;
        this.totalEarnedReward = totalEarnedReward;
        this.totalPossibleReward = totalPossibleReward;
        this.missions = missions;
    }

    public static MissionProgressResponse createResponse(Long challengeHistoryId, Long challengeId, String challengeTitle,
                                                         Integer totalMissionCount, Integer completedMissionCount,
                                                         Integer totalPossibleReward, Integer totalEarnedReward,
                                                         List<MissionHistoryResponse> missions) {

        return MissionProgressResponse.builder()
                .challengeHistoryId(challengeHistoryId)
                .challengeId(challengeId)
                .challengeTitle(challengeTitle)
                .totalMissionCount(totalMissionCount)
                .completedMissionCount(completedMissionCount)
                .totalEarnedReward(totalEarnedReward)
                .totalPossibleReward(totalPossibleReward)
                .missions(missions)
                .build();
    }
}
