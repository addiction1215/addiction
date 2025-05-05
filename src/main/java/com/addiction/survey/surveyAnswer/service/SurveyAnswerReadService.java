package com.addiction.survey.surveyAnswer.service;

import java.util.List;

import com.addiction.survey.surveyAnswer.entity.SurveyAnswer;

public interface SurveyAnswerReadService {
	SurveyAnswer findById(int answerId);

	int findScoreById(int answerId);

	int calculateTotalScore(List<Integer> answerId);
}
