package com.addiction.user.users.service.impl;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.addiction.global.security.SecurityService;
import com.addiction.survey.surveyAnswer.service.SurveyAnswerReadService;
import com.addiction.survey.surveyResult.service.SurveyResultReadService;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.repository.UserRepository;
import com.addiction.user.users.service.UserReadService;
import com.addiction.user.users.service.UserService;
import com.addiction.user.users.service.request.UserSaveServiceRequest;
import com.addiction.user.users.service.request.UserUpdatePurposeServiceRequest;
import com.addiction.user.users.service.request.UserUpdateServiceRequest;
import com.addiction.user.users.service.request.UserUpdateSurveyServiceRequest;
import com.addiction.user.users.service.response.UserSaveResponse;
import com.addiction.user.users.service.response.UserStartDateResponse;
import com.addiction.user.users.service.response.UserUpdatePurposeResponse;
import com.addiction.user.users.service.response.UserUpdateResponse;
import com.addiction.user.users.service.response.UserUpdateSurveyResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	private final SecurityService securityService;
	private final SurveyAnswerReadService surveyAnswerReadService;
	private final SurveyResultReadService surveyResultReadService;
	private final UserReadService userReadService;

	private final UserRepository userRepository;

	@Override
	public UserSaveResponse save(UserSaveServiceRequest userSaveServiceRequest) {
		return UserSaveResponse.createResponse(userRepository.save(userSaveServiceRequest.toEntity(bCryptPasswordEncoder)));
	}

	@Override
	public UserUpdateResponse update(UserUpdateServiceRequest userUpdateServiceRequest) {
		User user = userReadService.findById(securityService.getCurrentLoginUserInfo().getUserId());
		user.update(
			userUpdateServiceRequest.getSex(),
			userUpdateServiceRequest.getBirthDay()
		);
		return UserUpdateResponse.createResponse(user);
	}

	@Override
	public UserUpdateSurveyResponse updateSurvey(UserUpdateSurveyServiceRequest userUpdateSurveyServiceRequest) {
		User user = userReadService.findById(securityService.getCurrentLoginUserInfo().getUserId());
		int totalScore = surveyAnswerReadService.calculateTotalScore(userUpdateSurveyServiceRequest.getAnswerId());

		user.updateSurvey(
			userUpdateSurveyServiceRequest.getPurpose(),
			totalScore,
			userUpdateSurveyServiceRequest.getCigarettePrice(),
			LocalDateTime.now(),
			userUpdateSurveyServiceRequest.getSex(),
			userUpdateSurveyServiceRequest.getBirthDay()
		);

		return UserUpdateSurveyResponse.of(user, surveyResultReadService.findClosestScore(totalScore));
	}

	@Override
	public UserUpdatePurposeResponse updatePurpose(UserUpdatePurposeServiceRequest userUpdatePurposeServiceRequest) {
		User user = userReadService.findById(securityService.getCurrentLoginUserInfo().getUserId());
		user.updatePurpose(userUpdatePurposeServiceRequest.getPurpose());
		return UserUpdatePurposeResponse.createResponse(user);
	}

}
