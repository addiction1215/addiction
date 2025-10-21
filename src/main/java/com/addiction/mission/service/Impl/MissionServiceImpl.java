package com.addiction.mission.service.Impl;

import com.addiction.challenge.entity.Challenge;
import com.addiction.challenge.repository.ChallengeJpaRepository;
import com.addiction.global.security.SecurityService;
import com.addiction.mission.entity.Mission;
import com.addiction.mission.repository.MissionJpaRepository;
import com.addiction.mission.service.MissionService;
import com.addiction.mission.service.mission.request.MissionReportRequest;
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
}
