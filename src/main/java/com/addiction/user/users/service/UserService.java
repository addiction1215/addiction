package com.addiction.user.users.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.addiction.global.security.SecurityService;
import com.addiction.survey.surveyAnswer.service.SurveyAnswerReadService;
import com.addiction.survey.surveyResult.service.SurveyResultReadService;
import com.addiction.user.users.dto.service.request.UserSaveServiceRequest;
import com.addiction.user.users.dto.service.request.UserUpdateServiceRequest;
import com.addiction.user.users.dto.service.request.UserUpdateSurveyServiceRequest;
import com.addiction.user.users.dto.service.response.UserSaveResponse;
import com.addiction.user.users.dto.service.response.UserUpdateResponse;
import com.addiction.user.users.dto.service.response.UserUpdateSurveyResponse;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

	private final UserRepository userRepository;
	private final SecurityService securityService;
	private final SurveyAnswerReadService surveyAnswerReadService;
	private final SurveyResultReadService surveyResultReadService;

	public UserSaveResponse save(UserSaveServiceRequest userSaveServiceRequest) {
		return UserSaveResponse.createResponse(userRepository.save(userSaveServiceRequest.toEntity()));
	}

	public UserUpdateResponse update(UserUpdateServiceRequest userUpdateServiceRequest) {
		User user = userRepository.findById(securityService.getCurrentLoginUserInfo().getUserId());
		user.update(
			userUpdateServiceRequest.getSex(),
			userUpdateServiceRequest.getBirthDay()
		);
		return UserUpdateResponse.createResponse(user);
	}

	public UserUpdateSurveyResponse updateSurvey(UserUpdateSurveyServiceRequest userUpdateSurveyServiceRequest) {
		User user = userRepository.findById(securityService.getCurrentLoginUserInfo().getUserId());
		int totalScore = surveyAnswerReadService.calculateTotalScore(userUpdateSurveyServiceRequest.getAnswerId());

		user.updateSurvey(
			userUpdateSurveyServiceRequest.getPurpose(),
			totalScore,
			userUpdateSurveyServiceRequest.getCigarettePrice()
		);

		return UserUpdateSurveyResponse.of(user, surveyResultReadService.findClosestScore(totalScore));
	}

}
