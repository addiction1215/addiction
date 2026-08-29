package com.addiction.survey.surveyQuestion.service;

import com.addiction.survey.surveyQuestion.service.response.SurveyQuestionFindListServiceResponse;
import com.addiction.survey.surveyQuestion.entity.SurveyQuestion;
import com.addiction.survey.surveyQuestion.enums.SurveyType;

import java.util.List;

public interface SurveyQuestionReadService {

	SurveyQuestionFindListServiceResponse findAllByOrderBySortAsc();

    List<SurveyQuestion> findAllBySurveyTypeOrderBySortAsc(SurveyType surveyType);

    long count();

}
