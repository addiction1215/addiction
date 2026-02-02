package com.addiction.user.users.service.impl;

import com.addiction.storage.enums.BucketKind;
import com.addiction.storage.service.S3StorageService;
import com.addiction.user.users.service.response.UserInfoResponse;
import com.addiction.user.users.service.response.UserProfileResponse;
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

import com.addiction.user.users.service.response.UserSimpleProfileResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserReadServiceImpl implements UserReadService {

	private static final String UNKNOWN_USER = "해당 회원은 존재하지 않습니다.";

	private final SecurityService securityService;
    private final S3StorageService s3StorageService;

	private final UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findAllWithPushes() {
        return userRepository.findAllWithPushes();
    }

    @Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new AddictionException(UNKNOWN_USER));
	}

	@Override
	public User findByEmailAndNickName(String email, String nickName) {
		return userRepository.findByEmailAndNickName(email, nickName)
			.orElseThrow(() -> new AddictionException("입력하신 이메일과 닉네임에 해당하는 회원이 존재하지 않습니다."));
	}

	@Override
	public User findById(Long id) {
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

	@Override
	public UserProfileResponse findProfile() {
        User user = findById(securityService.getCurrentLoginUserInfo().getUserId());
		return UserProfileResponse.createResponse(
                user,
                s3StorageService.createPresignedUrl(user.getProfileUrl(), BucketKind.USER)
		);
	}

	@Override
	public UserInfoResponse findUserInfo() {
		return UserInfoResponse.createResponse(
				findById(securityService.getCurrentLoginUserInfo().getUserId())
		);
	}

	@Override
	public UserSimpleProfileResponse findSimpleProfile() {
        User user = findById(securityService.getCurrentLoginUserInfo().getUserId());
		return UserSimpleProfileResponse.createResponse(
                user,
                s3StorageService.createPresignedUrl(user.getProfileUrl(), BucketKind.USER)
		);
	}
}
