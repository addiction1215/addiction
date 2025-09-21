package com.addiction.survey.surveyResult.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.addiction.global.exception.AddictionException;
import com.addiction.survey.surveyResult.entity.SurveyResult;
import com.addiction.survey.surveyResult.repository.SurveyResultRepository;
import com.addiction.survey.surveyResult.service.SurveyResultReadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurveyResultReadServiceImpl implements SurveyResultReadService {

	private static final String UNKNOWN_SURVEY_RESULT = "점수가 일치하는 설문결과가 없습니다.";

	private final SurveyResultRepository surveyResultRepository;

	@Override
	public SurveyResult findClosestScore(int score) {
		return surveyResultRepository.findClosestScore(score)
			.orElseThrow(() -> new AddictionException(UNKNOWN_SURVEY_RESULT));
	}

}
