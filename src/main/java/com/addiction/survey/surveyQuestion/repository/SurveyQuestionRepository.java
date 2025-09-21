package com.addiction.survey.surveyQuestion.repository;

import java.util.List;

import com.addiction.survey.surveyQuestion.dto.repository.response.SurveyQuestionDto;
import com.addiction.survey.surveyQuestion.entity.SurveyQuestion;

public interface SurveyQuestionRepository {

	SurveyQuestion save(SurveyQuestion surveyQuestion);

	List<SurveyQuestion> findAllByOrderBySortAsc();

	void deleteAllInBatch();

}
