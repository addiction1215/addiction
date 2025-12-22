package com.addiction.mission.service.Impl;

import com.addiction.global.security.SecurityService;
import com.addiction.mission.repository.MissionRepository;
import com.addiction.mission.service.MissionReadService;
import com.addiction.mission.service.mission.response.MissionResponseList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MissionReadServiceImpl implements MissionReadService {
    private final SecurityService securityService;
    private final MissionRepository missionRepository;

    @Override
    public List<MissionResponseList> getMission(Long challengeId) {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();
        return missionRepository.findByChallengeIdAndUserId(challengeId, userId);
    }
}
