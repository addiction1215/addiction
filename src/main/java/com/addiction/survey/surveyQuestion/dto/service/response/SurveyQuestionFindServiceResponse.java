package com.addiction.survey.surveyQuestion.dto.service.response;

import java.util.List;

import com.addiction.survey.surveyAnswer.dto.service.response.SurveyAnswerFindServiceResponse;
import com.addiction.survey.surveyQuestion.entity.SurveyQuestion;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SurveyQuestionFindServiceResponse {

	private final int id;
	private final String question;
	private final List<SurveyAnswerFindServiceResponse> surveyAnswer;

	@Builder
	public SurveyQuestionFindServiceResponse(int id, String question,
		List<SurveyAnswerFindServiceResponse> surveyAnswer) {
		this.id = id;
		this.question = question;
		this.surveyAnswer = surveyAnswer;
	}

	public static SurveyQuestionFindServiceResponse of(SurveyQuestion surveyQuestion) {
		return SurveyQuestionFindServiceResponse.builder()
			.id(surveyQuestion.getId())
			.question(surveyQuestion.getQuestion())
			.surveyAnswer(
				surveyQuestion.getSurveyAnswers().stream().map(SurveyAnswerFindServiceResponse::of).toList()
			)
			.build();
	}
}
