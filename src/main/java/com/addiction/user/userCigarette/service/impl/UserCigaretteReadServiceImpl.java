package com.addiction.user.userCigarette.service.impl;

import java.time.LocalDateTime;
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
	private final SecurityService securityService;

	@Override
	public UserCigaretteFindResponse findUserCigaretteCount() {
		return UserCigaretteFindResponse.createResponse(
			userCigaretteRepository.cigaretteCountByUserId(securityService.getCurrentLoginUserInfo().getUserId())
		);
	}

	@Override
	public List<UserCigarette> findAll() {
		return userCigaretteRepository.findAll();
	}

	@Override
	public List<UserCigarette> findAllByCreatedDateBetween(LocalDateTime start, LocalDateTime end) {
		return userCigaretteRepository.findAllByCreatedDateBetween(start, end);
	}

	@Override
	public List<UserCigarette> findAllByUserIdAndCreatedDateBetween(Long userId, LocalDateTime start, LocalDateTime end) {
		return userCigaretteRepository.findAllByUserIdAndCreatedDateBetween(userId, start, end);
	}

	@Override
	public UserCigarette findLatestByUserId(Long userId) {
		return userCigaretteRepository.findTopByUserIdOrderByCreatedDateDesc(userId)
			.orElse(null);
	}
}
