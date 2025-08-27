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

	@Builder
	public UserUpdateSurveyResponse(String resultTitle, List<String> result) {
		this.resultTitle = resultTitle;
		this.result = result;
	}

	public static UserUpdateSurveyResponse of(SurveyResult surveyResult) {
		return UserUpdateSurveyResponse.builder()
			.resultTitle(surveyResult.getTitle())
			.result(
				surveyResult.getDescriptions().stream().map(SurveyResultDescription::getDescription).toList()
			)
			.build();
	}
}
