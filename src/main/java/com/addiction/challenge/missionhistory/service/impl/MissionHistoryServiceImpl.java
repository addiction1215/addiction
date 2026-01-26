package com.addiction.challenge.missionhistory.service.impl;

import com.addiction.challenge.mission.entity.Mission;
import com.addiction.challenge.mission.entity.MissionCategoryStatus;
import com.addiction.challenge.missionhistory.entity.MissionHistory;
import com.addiction.challenge.missionhistory.repository.MissionHistoryRepository;
import com.addiction.challenge.missionhistory.service.MissionHistoryReadService;
import com.addiction.challenge.missionhistory.service.MissionHistoryService;
import com.addiction.challenge.missionhistory.service.request.MissionSubmitServiceRequest;
import com.addiction.challenge.missionhistory.service.response.MissionDetailResponse;
import com.addiction.challenge.missionhistory.service.response.MissionSubmitResponse;
import com.addiction.storage.enums.BucketKind;
import com.addiction.storage.service.S3StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MissionHistoryServiceImpl implements MissionHistoryService {

    private final MissionHistoryReadService missionHistoryReadService;
    private final S3StorageService s3StorageService;

    private final MissionHistoryRepository missionHistoryRepository;

    @Override
    public List<MissionHistory> saveAll(List<MissionHistory> missionHistories) {
        return missionHistoryRepository.saveAll(missionHistories);
    }

    @Override
    public MissionDetailResponse getMissionDetail(Long missionHistoryId) {
        MissionHistory missionHistory = missionHistoryReadService.findById(missionHistoryId);
        return MissionDetailResponse.createResponse(
                missionHistory,
                s3StorageService.createPresignedUrl(missionHistory.getPhotoUrl1(), BucketKind.CHALLENGE),
                s3StorageService.createPresignedUrl(missionHistory.getPhotoUrl2(), BucketKind.CHALLENGE),
                s3StorageService.createPresignedUrl(missionHistory.getPhotoUrl3(), BucketKind.CHALLENGE)
        );
    }

    @Override
    public MissionSubmitResponse submitMission(MissionSubmitServiceRequest request) {
        MissionHistory missionHistory = missionHistoryReadService.findById(request.getMissionHistoryId());

        Mission mission = missionHistory.getMission();
        MissionCategoryStatus category = mission.getCategory();

        // 미션 타입에 따른 검증 및 저장
        switch (category) {
            case LOCATION -> missionHistory.submitGps(request.getAddress());
            case REPLACE_ACTION -> missionHistory.submitPhoto(request.getPhotoUrl());
            case HOLD -> missionHistory.submitAbstinenceTime(request.getTime());
        }

        return MissionSubmitResponse.createResponse(missionHistory.getId());
    }

    @Override
    public MissionSubmitResponse completeMission(Long missionHistoryId) {
        MissionHistory missionHistory = missionHistoryRepository.findById(missionHistoryId)
                .orElseThrow(() -> new IllegalArgumentException("미션 이력을 찾을 수 없습니다."));

        Mission mission = missionHistory.getMission();
        MissionCategoryStatus category = mission.getCategory();

        // 미션 타입별 검증
        switch (category) {
            case LOCATION -> missionHistory.locationVerify();
            case REPLACE_ACTION -> missionHistory.photoVerify();
            case HOLD -> missionHistory.abstinenceTimeVerify();
        }

        // 미션 완료 처리
        missionHistory.complete();

        return MissionSubmitResponse.createResponse(missionHistory.getId());
    }
}
