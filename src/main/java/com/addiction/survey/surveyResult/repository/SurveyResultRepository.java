package com.addiction.survey.surveyResult.repository;

import java.util.Optional;

import com.addiction.survey.surveyResult.entity.SurveyResult;

public interface SurveyResultRepository {

	SurveyResult save(SurveyResult surveyResult);

	Optional<SurveyResult> findClosestScore(int score);

	void deleteAllInBatch();

}
