package com.addiction.user.users.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.addiction.global.security.SecurityService;
import com.addiction.user.users.dto.service.request.UserUpdateServiceRequest;
import com.addiction.user.users.dto.service.response.UserUpdateResponse;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

	private final UserRepository userRepository;
	private final SecurityService securityService;

	public UserUpdateResponse update(UserUpdateServiceRequest userUpdateServiceRequest) {
		User user = userRepository.findById(securityService.getCurrentLoginUserInfo().getUserId());
		user.update(
			userUpdateServiceRequest.getPhoneNumber(),
			userUpdateServiceRequest.getSex(),
			userUpdateServiceRequest.getBirthDay()
		);
		return UserUpdateResponse.createResponse(user);
	}

}
