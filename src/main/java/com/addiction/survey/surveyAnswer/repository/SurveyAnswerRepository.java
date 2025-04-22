package com.addiction.survey.surveyAnswer.repository;

import com.addiction.survey.surveyAnswer.entity.SurveyAnswer;

public interface SurveyAnswerRepository {
	SurveyAnswer save(SurveyAnswer surveyAnswer);

	SurveyAnswer findById(int id);

	void deleteAllInBatch();
}
