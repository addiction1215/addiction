package com.addiction.challenge.challengehistory.service.Impl;

import com.addiction.challenge.challange.entity.Challenge;
import com.addiction.challenge.challange.service.ChallengeReadService;
import com.addiction.challenge.challengehistory.controller.request.ChallengeCancelRequest;
import com.addiction.challenge.challengehistory.controller.request.ChallengeJoinRequest;
import com.addiction.challenge.challengehistory.entity.ChallengeHistory;
import com.addiction.challenge.challengehistory.repository.ChallengeHistoryJpaRepository;
import com.addiction.challenge.challengehistory.service.ChallengeHistoryReadService;
import com.addiction.challenge.challengehistory.service.ChallengeHistoryService;
import com.addiction.challenge.challengehistory.service.response.ChallengeCancelResponse;
import com.addiction.challenge.challengehistory.service.response.ChallengeJoinResponse;
import com.addiction.challenge.mission.entity.Mission;
import com.addiction.challenge.mission.service.MissionReadService;
import com.addiction.challenge.missionhistory.entity.MissionHistory;
import com.addiction.challenge.missionhistory.service.MissionHistoryService;
import com.addiction.common.enums.ChallengeStatus;
import com.addiction.common.enums.MissionStatus;
import com.addiction.global.security.SecurityService;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.service.UserReadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChallengeHistoryServiceImpl implements ChallengeHistoryService {
    private final SecurityService securityService;
    private final ChallengeReadService challengeReadService;
    private final MissionReadService missionReadService;
    private final MissionHistoryService missionHistoryService;
    private final UserReadService userReadService;
    private final ChallengeHistoryReadService challengeHistoryReadService;

    private final ChallengeHistoryJpaRepository challengeHistoryJpaRepository;

    @Override
    public ChallengeJoinResponse joinChallenge(ChallengeJoinRequest request) {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();

        log.info("챌린지 참여 시작 - challengeId: {}, userId: {}", request.getChallengeId(), userId);

        // 1. 챌린지 존재 확인
        Challenge challenge = challengeReadService.findById(request.getChallengeId());

        // 2. 사용자 조회
        User user = userReadService.findById(userId);

        // 3. ChallengeHistory 생성
        ChallengeHistory challengeHistory = ChallengeHistory.createEntity(challenge, user, ChallengeStatus.PROGRESSING);
        challengeHistoryJpaRepository.save(challengeHistory);

        log.info("ChallengeHistory 생성 완료 - challengeHistoryId: {}", challengeHistory.getId());

        // 4. 챌린지에 속한 모든 미션 조회
        List<Mission> missions = missionReadService.findByChallengeId(request.getChallengeId());

        // 5. 각 미션에 대한 MissionHistory 생성
        List<MissionHistory> missionHistories = missions.stream()
                .map(mission -> MissionHistory.createEntity(mission, challengeHistory, user, MissionStatus.PROGRESSING))
                .collect(Collectors.toList());

        missionHistoryService.saveAll(missionHistories);

        log.info("MissionHistory 생성 완료 - 미션 개수: {}", missionHistories.size());

        // 6. Response 생성
        return ChallengeJoinResponse.of(challengeHistory, missionHistories);
    }

    @Override
    public ChallengeCancelResponse cancelChallenge(ChallengeCancelRequest request) {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();

        log.info("챌린지 포기 시작 - challengeHistoryId: {}, userId: {}", request.getChallengeHistoryId(), userId);

        // 1. ChallengeHistory 조회
        ChallengeHistory challengeHistory = challengeHistoryReadService.findById(request.getChallengeHistoryId());

        // 2. 사용자 권한 확인
        challengeHistory.confirmUser(userId);

        // 3. 이미 포기한 챌린지인지 확인
        challengeHistory.confirmCancel();

        // 4. 완료된 챌린지인지 확인
        challengeHistory.confirmComplete();

        // 5. 상태를 CANCELLED로 변경
        challengeHistory.updateStatus(ChallengeStatus.CANCELLED);

        log.info("챌린지 포기 완료 - challengeHistoryId: {}", challengeHistory.getId());

        // 6. Response 생성 (ID만 반환)
        return ChallengeCancelResponse.of(challengeHistory.getId());
    }

}
