package com.addiction.survey.surveyQuestion.service;

import com.addiction.survey.surveyQuestion.dto.service.response.SurveyQuestionFindListServiceResponse;

public interface SurveyQuestionReadService {

	SurveyQuestionFindListServiceResponse findAllByOrderBySortAsc();

}
