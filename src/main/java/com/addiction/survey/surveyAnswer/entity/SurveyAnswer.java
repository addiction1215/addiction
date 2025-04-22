package com.addiction.survey.surveyAnswer.entity;

import com.addiction.survey.surveyQuestion.entity.SurveyQuestion;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyAnswer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	private SurveyQuestion surveyQuestion;

	private String answer;

	private int score;

	@Builder
	public SurveyAnswer(SurveyQuestion surveyQuestion, String answer, int score) {
		this.surveyQuestion = surveyQuestion;
		this.answer = answer;
		this.score = score;
	}
}
