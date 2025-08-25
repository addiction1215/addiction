package com.addiction.survey.surveyAnswer.dto.service.response;

import com.addiction.survey.surveyAnswer.entity.SurveyAnswer;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SurveyAnswerFindServiceResponse {
	private final Long id;
	private final String answer;

	@Builder
	public SurveyAnswerFindServiceResponse(Long id, String answer) {
		this.id = id;
		this.answer = answer;
	}

	public static SurveyAnswerFindServiceResponse of(SurveyAnswer surveyAnswer) {
		return SurveyAnswerFindServiceResponse.builder()
			.id(surveyAnswer.getId())
			.answer(surveyAnswer.getAnswer())
			.build();
	}
}
