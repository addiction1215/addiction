package com.addiction.challenge.missionhistory.service;

import com.addiction.challenge.missionhistory.entity.MissionHistory;
import com.addiction.challenge.missionhistory.service.request.MissionSubmitServiceRequest;
import com.addiction.challenge.missionhistory.service.response.MissionDetailResponse;
import com.addiction.challenge.missionhistory.service.response.MissionSubmitResponse;

import java.util.List;

public interface MissionHistoryService {

    List<MissionHistory> saveAll(List<MissionHistory> missionHistories);

    /**
     * 미션 상세 조회
     */
    MissionDetailResponse getMissionDetail(Long missionHistoryId);

    /**
     * 미션 중간 제출
     */
    MissionSubmitResponse submitMission(MissionSubmitServiceRequest request);

    /**
     * 미션 최종 제출
     */
    MissionSubmitResponse completeMission(Long missionHistoryId);

}
