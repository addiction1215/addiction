package com.addiction.challenge.missionhistory.service.response;

import com.addiction.challenge.mission.entity.Mission;
import com.addiction.challenge.mission.entity.MissionCategoryStatus;
import com.addiction.challenge.missionhistory.entity.MissionHistory;
import com.addiction.challenge.missionhistory.entity.MissionStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MissionDetailResponse {
    private final Long missionId;
    private final String title;
    private final String content;
    private final Integer reward;
    private final MissionCategoryStatus category;
    private final Long missionHistoryId;
    private final MissionStatus status;
    private final LocalDateTime completeAt;
    private final String address;
    private final Integer gpsVerifyCount;
    private final String photoUrl1;
    private final String photoUrl2;
    private final String photoUrl3;
    private final Integer abstinenceTime;

    @Builder
    public MissionDetailResponse(Long missionId, String title, String content, Integer reward,
                                 MissionCategoryStatus category,
                                 Long missionHistoryId, MissionStatus status, LocalDateTime completeAt, String address,
                                 Integer gpsVerifyCount, String photoUrl1, String photoUrl2, String photoUrl3, Integer abstinenceTime) {
        this.missionId = missionId;
        this.title = title;
        this.content = content;
        this.reward = reward;
        this.category = category;
        this.missionHistoryId = missionHistoryId;
        this.status = status;
        this.completeAt = completeAt;
        this.address = address;
        this.gpsVerifyCount = gpsVerifyCount;
        this.photoUrl1 = photoUrl1;
        this.photoUrl2 = photoUrl2;
        this.photoUrl3 = photoUrl3;
        this.abstinenceTime = abstinenceTime;
    }

    public static MissionDetailResponse createResponse(MissionHistory missionHistory, String photoUrl1, String photoUrl2, String photoUrl3) {
        Mission mission = missionHistory.getMission();
        return MissionDetailResponse.builder()
                .missionId(mission.getId())
                .title(mission.getTitle())
                .content(mission.getContent())
                .reward(mission.getReward())
                .category(mission.getCategory())
                .missionHistoryId(missionHistory.getId())
                .status(missionHistory.getStatus())
                .completeAt(missionHistory.getCompleteAt())
                .address(missionHistory.getAddress())
                .gpsVerifyCount(missionHistory.getGpsVerifyCount())
                .photoUrl1(photoUrl1)
                .photoUrl2(photoUrl2)
                .photoUrl3(photoUrl3)
                .abstinenceTime(missionHistory.getAbstinenceTime())
                .build();
    }
}
