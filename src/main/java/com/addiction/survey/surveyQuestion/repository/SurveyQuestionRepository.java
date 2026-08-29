package com.addiction.survey.surveyQuestion.repository;

import java.util.List;

import com.addiction.survey.surveyQuestion.entity.SurveyQuestion;
import com.addiction.survey.surveyQuestion.enums.SurveyType;

public interface SurveyQuestionRepository {

	SurveyQuestion save(SurveyQuestion surveyQuestion);

	List<SurveyQuestion> findAllByOrderBySortAsc();

    List<SurveyQuestion> findAllBySurveyTypeOrderBySortAsc(SurveyType surveyType);

    long count();

	void deleteAllInBatch();

}
