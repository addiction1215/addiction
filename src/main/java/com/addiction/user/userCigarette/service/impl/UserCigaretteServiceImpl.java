package com.addiction.user.userCigarette.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.addiction.global.security.SecurityService;
import com.addiction.user.userCigarette.entity.UserCigarette;
import com.addiction.user.userCigarette.service.UserCigaretteReadService;
import com.addiction.user.userCigarette.service.UserCigaretteService;
import com.addiction.user.userCigarette.service.request.ChangeType;
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

	@Override
	public UserCigaretteFindResponse changeCigaretteCount(ChangeType changeType) {
		User user = userReadService.findById(securityService.getCurrentLoginUserInfo().getUserId());
		UserCigarette userCigarette = userCigaretteReadService.findByUserId(user.getId());
		changeType.changeCount(userCigarette);

		return UserCigaretteFindResponse.createResponse(userCigarette.getCount());
	}
}
