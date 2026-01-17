package com.addiction.survey.surveyQuestion.service.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SurveyQuestionFindListServiceResponse {

	private final List<SurveyQuestionFindServiceResponse> response;

	@Builder
	public SurveyQuestionFindListServiceResponse(List<SurveyQuestionFindServiceResponse> response) {
		this.response = response;
	}

	public static SurveyQuestionFindListServiceResponse of(List<SurveyQuestionFindServiceResponse> response) {
		return SurveyQuestionFindListServiceResponse.builder()
			.response(response)
			.build();
	}
}
