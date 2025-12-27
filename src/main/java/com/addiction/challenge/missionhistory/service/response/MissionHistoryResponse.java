package com.addiction.challenge.missionhistory.service.response;

import com.addiction.common.enums.MissionCategoryStatus;
import com.addiction.common.enums.MissionStatus;
import com.addiction.challenge.missionhistory.entity.MissionHistory;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MissionHistoryResponse {
    private final Long missionHistoryId;
    private final Long missionId;
    private final String missionTitle;
    private final String missionContent;
    private final MissionCategoryStatus category;
    private final Integer reward;
    private final MissionStatus status;

    @Builder
    public MissionHistoryResponse(Long missionHistoryId, Long missionId, String missionTitle, String missionContent, MissionCategoryStatus category, Integer reward, MissionStatus status) {
        this.missionHistoryId = missionHistoryId;
        this.missionId = missionId;
        this.missionTitle = missionTitle;
        this.missionContent = missionContent;
        this.category = category;
        this.reward = reward;
        this.status = status;
    }

    public static MissionHistoryResponse createResponse(MissionHistory missionHistory) {
        return MissionHistoryResponse.builder()
                .missionHistoryId(missionHistory.getId())
                .missionId(missionHistory.getMission().getId())
                .missionTitle(missionHistory.getMission().getTitle())
                .missionContent(missionHistory.getMission().getContent())
                .category(missionHistory.getMission().getCategory())
                .reward(missionHistory.getMission().getReward())
                .status(missionHistory.getStatus())
                .build();
    }
}
