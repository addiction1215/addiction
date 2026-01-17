package com.addiction.survey.surveyQuestion.repository.response;

import java.util.List;

import com.addiction.survey.surveyAnswer.repository.response.SurveyAnswerDto;

public class SurveyQuestionDto {

	private int id;
	private String question;
	private List<SurveyAnswerDto> surveyAnswer;

	public SurveyQuestionDto(int id, String question, List<SurveyAnswerDto> surveyAnswer) {
		this.id = id;
		this.question = question;
		this.surveyAnswer = surveyAnswer;
	}
}
