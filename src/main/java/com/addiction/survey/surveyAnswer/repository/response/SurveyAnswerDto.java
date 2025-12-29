package com.addiction.survey.surveyAnswer.repository.response;

import lombok.Builder;

public class SurveyAnswerDto {
	private int id;
	private String answer;

	@Builder
	public SurveyAnswerDto(int id, String answer) {
		this.id = id;
		this.answer = answer;
	}
}
