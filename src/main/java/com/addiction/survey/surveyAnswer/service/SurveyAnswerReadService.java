package com.addiction.survey.surveyAnswer.service;

import java.util.List;

import com.addiction.survey.surveyAnswer.entity.SurveyAnswer;

public interface SurveyAnswerReadService {
	SurveyAnswer findById(Long answerId);

    List<SurveyAnswer> findAllByIdInWithSurveyQuestion(List<Long> answerIds);

	int findScoreById(Long answerId);

	int calculateTotalScore(List<Long> answerId);
}
