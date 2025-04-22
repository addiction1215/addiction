package com.addiction.survey.surveyResult.repository;

import com.addiction.survey.surveyResult.entity.SurveyResult;

public interface SurveyResultRepository {

	SurveyResult save(SurveyResult surveyResult);

	SurveyResult findClosestScore(int score);

	void deleteAllInBatch();

}
