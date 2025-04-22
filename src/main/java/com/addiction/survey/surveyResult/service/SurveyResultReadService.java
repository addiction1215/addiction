package com.addiction.survey.surveyResult.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.addiction.survey.surveyResult.entity.SurveyResult;
import com.addiction.survey.surveyResult.repository.SurveyResultRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurveyResultReadService {

	private final SurveyResultRepository surveyResultRepository;

	public SurveyResult findClosestScore(int score) {
		return surveyResultRepository.findClosestScore(score);
	}

}
