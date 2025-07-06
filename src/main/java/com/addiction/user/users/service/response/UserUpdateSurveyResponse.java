package com.addiction.user.users.service.response;

import java.util.List;

import com.addiction.survey.surveyResult.entity.SurveyResult;
import com.addiction.survey.surveyResultDescription.entity.SurveyResultDescription;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.Sex;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserUpdateSurveyResponse {
	private final String resultTitle;
	private final List<String> result;
	private final Sex sex;
	private final String birthDay;

	@Builder
	public UserUpdateSurveyResponse(String resultTitle, List<String> result, Sex sex, String birthDay) {
		this.resultTitle = resultTitle;
		this.result = result;
		this.sex = sex;
		this.birthDay = birthDay;
	}

	public static UserUpdateSurveyResponse of(User user, SurveyResult surveyResult) {
		return UserUpdateSurveyResponse.builder()
			.resultTitle(surveyResult.getTitle())
			.result(
				surveyResult.getDescriptions().stream().map(SurveyResultDescription::getDescription).toList()
			)
			.sex(user.getSex())
			.birthDay(user.getBirthDay())
			.build();
	}
}
