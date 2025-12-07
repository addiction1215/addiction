package com.addiction.challenge.service.Impl;

import com.addiction.challenge.entity.Challenge;
import com.addiction.challenge.repository.ChallengeJpaRepository;
import com.addiction.challenge.service.ChallengeService;
import com.addiction.challenge.service.challenge.request.FailChallengeRequest;
import com.addiction.common.enums.MissionStatus;
import com.addiction.global.security.SecurityService;
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
public class ChallengeServiceImpl implements ChallengeService {
    private final SecurityService securityService;
	private final ChallengeJpaRepository challengeRepository;
	private final MissionHistoryJpaRepository missionHistoryJpaRepository;
	private final UserJpaRepository userRepository;

    @Override
    public void updateFailChallenge(FailChallengeRequest request) {
        long userId = securityService.getCurrentLoginUserInfo().getUserId();

		Challenge challenge = challengeRepository.getReferenceById(request.getChallengeId());
		User user = userRepository.getReferenceById(userId);

		MissionHistory missionHistory = MissionHistory.updateMissionHistoryForFail(challenge, MissionStatus.CANCELLED, user);

		missionHistoryJpaRepository.save(missionHistory);

    }
}
