package com.addiction.user.users.service.response;

import java.util.List;

import com.addiction.survey.surveyResult.entity.SurveyResult;
import com.addiction.survey.surveyResultDescription.entity.SurveyResultDescription;
import com.addiction.user.users.entity.User;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserUpdateSurveyResponse {
	private final String nickName;
	private final String resultTitle;
	private final List<String> result;

	@Builder
	public UserUpdateSurveyResponse(String nickName, String resultTitle, List<String> result) {
		this.nickName = nickName;
		this.resultTitle = resultTitle;
		this.result = result;
	}

	public static UserUpdateSurveyResponse of(User user, SurveyResult surveyResult) {
		return UserUpdateSurveyResponse.builder()
			.nickName(user.getNickName())
			.resultTitle(surveyResult.getTitle())
			.result(
				surveyResult.getDescriptions().stream().map(SurveyResultDescription::getDescription).toList()
			)
			.build();
	}
}
