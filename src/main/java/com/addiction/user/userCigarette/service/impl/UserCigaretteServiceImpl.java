package com.addiction.user.userCigarette.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.addiction.global.security.SecurityService;
import com.addiction.user.userCigarette.entity.UserCigarette;
import com.addiction.user.userCigarette.repository.UserCigaretteRepository;
import com.addiction.user.userCigarette.service.UserCigaretteReadService;
import com.addiction.user.userCigarette.service.UserCigaretteService;
import com.addiction.user.userCigarette.service.request.ChangeType;
import com.addiction.user.userCigarette.service.request.UserCigaretteChangeServiceRequest;
import com.addiction.user.userCigarette.service.response.UserCigaretteFindResponse;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.service.UserReadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCigaretteServiceImpl implements UserCigaretteService {

	private final UserCigaretteReadService userCigaretteReadService;
	private final SecurityService securityService;
	private final UserReadService userReadService;

	private final UserCigaretteRepository userCigaretteRepository;

	@Override
	public Long changeCigarette(UserCigaretteChangeServiceRequest userCigaretteChangeServiceRequest) {
		User user = userReadService.findById(securityService.getCurrentLoginUserInfo().getUserId());
		if (userCigaretteChangeServiceRequest.getChangeType().equals(ChangeType.ADD)) {
			// 바로 전 흡연 기록 조회 (최신 1건)
			UserCigarette lastCigarette = userCigaretteReadService.findLatestByUserId(user.getId());

			long intervalMinutes = 0;

			if (lastCigarette != null) {
				intervalMinutes = java.time.Duration.between(
					lastCigarette.getCreatedDate(), java.time.LocalDateTime.now()
				).toMinutes();
			}
			UserCigarette userCigarette = UserCigarette.createEntity(
				user, userCigaretteChangeServiceRequest.getAddress(), intervalMinutes
			);
			userCigaretteRepository.save(userCigarette);
			return user.getId();
		}
		userCigaretteRepository.deleteLastest(user.getId());
		return user.getId();
	}
}
