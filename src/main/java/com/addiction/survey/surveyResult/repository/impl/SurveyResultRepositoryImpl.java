package com.addiction.survey.surveyResult.repository.impl;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.addiction.global.exception.AddictionException;
import com.addiction.survey.surveyResult.entity.SurveyResult;
import com.addiction.survey.surveyResult.repository.SurveyResultJpaRepository;
import com.addiction.survey.surveyResult.repository.SurveyResultRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SurveyResultRepositoryImpl implements SurveyResultRepository {

	private final SurveyResultJpaRepository surveyResultJpaRepository;

	@Override
	public SurveyResult save(SurveyResult surveyResult) {
		return surveyResultJpaRepository.save(surveyResult);
	}

	@Override
	public Optional<SurveyResult> findClosestScore(int score) {
		return surveyResultJpaRepository.findClosestScore(score);
	}

	@Override
	public void deleteAllInBatch() {
		surveyResultJpaRepository.deleteAllInBatch();
	}
}
