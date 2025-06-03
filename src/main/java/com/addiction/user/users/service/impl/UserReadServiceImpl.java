package com.addiction.user.users.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.addiction.global.exception.AddictionException;
import com.addiction.global.security.SecurityService;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.repository.UserRepository;
import com.addiction.user.users.service.UserReadService;
import com.addiction.user.users.service.response.UserPurposeResponse;
import com.addiction.user.users.service.response.UserStartDateResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserReadServiceImpl implements UserReadService {

	private static final String UNKNOWN_USER = "해당 회원은 존재하지 않습니다.";

	private final SecurityService securityService;

	private final UserRepository userRepository;

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new AddictionException(UNKNOWN_USER));
	}

	@Override
	public User findById(int id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new AddictionException(UNKNOWN_USER));
	}

	@Override
	public UserStartDateResponse findStartDate() {
		return UserStartDateResponse.createResponse(
			findById(securityService.getCurrentLoginUserInfo().getUserId())
		);
	}

	@Override
	public UserPurposeResponse findPurpose() {
		return UserPurposeResponse.createResponse(
			findById(securityService.getCurrentLoginUserInfo().getUserId())
		);
	}
}
