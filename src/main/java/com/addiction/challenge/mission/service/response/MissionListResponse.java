package com.addiction.challenge.mission.service.response;

import com.addiction.challenge.challange.entity.Challenge;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MissionListResponse {
    private final Long challengeId;
    private final String challengeTitle;
    private final Integer totalMissionCount;
    private final Integer totalReward;
    private final List<MissionResponse> missions;

    @Builder
    public MissionListResponse(Long challengeId, String challengeTitle,
                                       Integer totalMissionCount, Integer totalReward,
                                       List<MissionResponse> missions) {
        this.challengeId = challengeId;
        this.challengeTitle = challengeTitle;
        this.totalMissionCount = totalMissionCount;
        this.totalReward = totalReward;
        this.missions = missions;
    }

    public static MissionListResponse createResponse(Challenge challenge, List<MissionResponse> missions, Integer totalReward) {
        return MissionListResponse.builder()
                .challengeId(challenge.getId())
                .challengeTitle(challenge.getTitle())
                .totalMissionCount(missions.size())
                .totalReward(totalReward)
                .missions(missions)
                .build();
    }
}
