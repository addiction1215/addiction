package com.addiction.survey.surveyResult.service;

import com.addiction.survey.surveyResult.entity.SurveyResult;

public interface SurveyResultReadService {

	SurveyResult findClosestScore(int score);

}
