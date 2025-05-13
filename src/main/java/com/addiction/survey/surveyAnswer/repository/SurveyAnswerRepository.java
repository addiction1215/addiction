package com.addiction.survey.surveyAnswer.repository;

import java.util.Optional;

import com.addiction.survey.surveyAnswer.entity.SurveyAnswer;

public interface SurveyAnswerRepository {
	SurveyAnswer save(SurveyAnswer surveyAnswer);

	Optional<SurveyAnswer> findById(int id);

	void deleteAllInBatch();
}
