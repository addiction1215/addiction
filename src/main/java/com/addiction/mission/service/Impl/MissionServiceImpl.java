package com.addiction.mission.service.Impl;

import com.addiction.challenge.entity.Challenge;
import com.addiction.challenge.repository.ChallengeJpaRepository;
import com.addiction.common.enums.MissionCategoryStatus;
import com.addiction.common.enums.MissionStatus;
import com.addiction.global.security.SecurityService;
import com.addiction.mission.entity.Mission;
import com.addiction.mission.repository.MissionJpaRepository;
import com.addiction.mission.service.MissionService;
import com.addiction.mission.service.mission.request.MissionCompleteRequest;
import com.addiction.mission.service.mission.request.MissionReportRequest;
import com.addiction.mission.service.mission.request.MissionStartRequest;
import com.addiction.mission.service.mission.response.MissionDetailResponse;
import com.addiction.mission.service.mission.response.MissionHistoryResponse;
import com.addiction.missionhistory.entity.MissionHistory;
import com.addiction.missionhistory.repository.MissionHistoryJpaRepository;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.repository.UserJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class MissionServiceImpl implements MissionService {
    private final SecurityService securityService;
    private final MissionHistoryJpaRepository missionHistoryJpaRepository;
    private final MissionJpaRepository missionRepository;
    private final ChallengeJpaRepository challengeRepository;
    private final UserJpaRepository userRepository;

    @Override
    public void insertMissionReport(MissionReportRequest request) {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();

        Mission mission = missionRepository.getReferenceById(request.getMissionId());
        Challenge challenge = challengeRepository.getReferenceById(request.getChallengeId());
        User user = userRepository.getReferenceById(userId);

        MissionHistory missionHistory = MissionHistory.createMissionHistory(
                mission, challenge, request.getStatus(), request.getAccTime(), request.getAddress(), user);

        missionHistoryJpaRepository.save(missionHistory);
    }

    @Override
    public MissionDetailResponse getMissionDetail(Long missionId) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new IllegalArgumentException("미션을 찾을 수 없습니다."));
        
        return MissionDetailResponse.from(mission);
    }

    @Override
    public MissionHistoryResponse startMission(MissionStartRequest request) {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();

        Mission mission = missionRepository.findById(request.getMissionId())
                .orElseThrow(() -> new IllegalArgumentException("미션을 찾을 수 없습니다."));
        
        Challenge challenge = challengeRepository.findById(request.getChallengeId())
                .orElseThrow(() -> new IllegalArgumentException("챌린지를 찾을 수 없습니다."));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 진행 중인 미션이 있는지 확인
        missionHistoryJpaRepository.findByUserIdAndStatus(user, MissionStatus.PROGRESSING)
                .ifPresent(history -> {
                    throw new IllegalStateException("이미 진행 중인 미션이 있습니다.");
                });

        // 미션 이력 생성
        MissionHistory missionHistory = MissionHistory.createMissionHistory(
                mission, challenge, MissionStatus.PROGRESSING, 
                0, // 초기 누적 시간 0
                null, user);

        MissionHistory saved = missionHistoryJpaRepository.save(missionHistory);
        
        return MissionHistoryResponse.from(saved);
    }

    @Override
    public MissionHistoryResponse completeMission(MissionCompleteRequest request) {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();

        MissionHistory missionHistory = missionHistoryJpaRepository.findById(request.getMissionHistoryId())
                .orElseThrow(() -> new IllegalArgumentException("미션 이력을 찾을 수 없습니다."));

        // 권한 확인
        if (!missionHistory.getUserId().getId().equals(userId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        // 이미 완료된 미션인지 확인
        if (missionHistory.getStatus() == MissionStatus.COMPLETED) {
            throw new IllegalStateException("이미 완료된 미션입니다.");
        }

        Mission mission = missionHistory.getMissionId();
        MissionCategoryStatus category = mission.getCategory();

        // 타입별 처리
        String addressData = null;
        Integer accTime = missionHistory.getAccTime();

        switch (category) {
            case HOLD: // 참기 (타이머)
                if (request.getAccTime() != null) {
                    accTime = request.getAccTime();
                }
                break;
                
            case REPLACE_ACTION: // 대체 행동 (인증샷)
                if (request.getPhotoUrl() == null) {
                    throw new IllegalArgumentException("인증샷이 필요합니다.");
                }
                addressData = request.getPhotoUrl();
                break;
                
            case LOCATION: // 위치 인증 (GPS + 인증샷)
                if (request.getPhotoUrl() == null) {
                    throw new IllegalArgumentException("인증샷이 필요합니다.");
                }
                if (request.getLatitude() == null || request.getLongitude() == null) {
                    throw new IllegalArgumentException("GPS 정보가 필요합니다.");
                }
                // GPS 정보 + 인증샷 URL을 JSON 형식으로 저장
                addressData = String.format("{\"lat\":%f,\"lng\":%f,\"photo\":\"%s\"}", 
                        request.getLatitude(), request.getLongitude(), request.getPhotoUrl());
                break;
        }

        // 미션 완료 처리
        MissionHistory updated = MissionHistory.createMissionHistory(
                mission, 
                missionHistory.getChallengeId(), 
                MissionStatus.COMPLETED, 
                accTime, 
                addressData, 
                missionHistory.getUserId());

        // ID 설정하여 업데이트
        missionHistoryJpaRepository.save(MissionHistory.builder()
                .id(missionHistory.getId())
                .missionId(mission)
                .challengeId(missionHistory.getChallengeId())
                .status(MissionStatus.COMPLETED)
                .accTime(accTime)
                .address(addressData)
                .userId(missionHistory.getUserId())
                .build());

        MissionHistory completed = missionHistoryJpaRepository.findById(missionHistory.getId())
                .orElseThrow(() -> new IllegalArgumentException("미션 이력을 찾을 수 없습니다."));

        return MissionHistoryResponse.from(completed);
    }

    @Override
    public MissionHistoryResponse getProgressingMission() {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        MissionHistory missionHistory = missionHistoryJpaRepository
                .findByUserIdAndStatus(user, MissionStatus.PROGRESSING)
                .orElse(null);

        if (missionHistory == null) {
            return null;
        }

        return MissionHistoryResponse.from(missionHistory);
    }
}
