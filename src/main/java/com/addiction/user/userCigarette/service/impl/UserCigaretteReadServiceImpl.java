package com.addiction.user.userCigarette.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.addiction.global.security.SecurityService;
import com.addiction.user.userCigarette.entity.UserCigarette;
import com.addiction.user.userCigarette.repository.UserCigaretteRepository;
import com.addiction.user.userCigarette.service.UserCigaretteReadService;
import com.addiction.user.userCigarette.service.response.UserCigaretteFindResponse;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.service.UserReadService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserCigaretteReadServiceImpl implements UserCigaretteReadService {

	private final UserCigaretteRepository userCigaretteRepository;
	private final UserReadService userReadService;
	private final SecurityService securityService;

	@Override
	public UserCigarette findByUserId(int userId) {
		return userCigaretteRepository.findByUserId(userId)
			.orElse(userCigaretteRepository.save(UserCigarette.createEntity(userReadService.findById(userId))));
	}

	@Override
	public UserCigaretteFindResponse findUserCigaretteCount() {
		User user = userReadService.findById(securityService.getCurrentLoginUserInfo().getUserId());
		UserCigarette userCigarette = findByUserId(user.getId());
		return UserCigaretteFindResponse.createResponse(userCigarette.getCount());
	}

	@Override
	public List<UserCigarette> findAll() {
		return List.of();
	}
}
