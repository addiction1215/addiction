package com.addiction.survey.surveyResultDescription.repository;

import com.addiction.survey.surveyResultDescription.entity.SurveyResultDescription;

public interface SurveyResultDescriptionRepository {

	SurveyResultDescription save(SurveyResultDescription surveyResultDescription);

	void deleteAllInBatch();

}
