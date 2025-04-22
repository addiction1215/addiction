package com.addiction.survey.surveyResult.repository;

import org.springframework.stereotype.Repository;

import com.addiction.global.exception.AddictionException;
import com.addiction.survey.surveyAnswer.entity.SurveyAnswer;
import com.addiction.survey.surveyResult.entity.SurveyResult;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SurveyResultRepositoryImpl implements SurveyResultRepository {

	private static final String UNKNOWN_SURVEY_RESULT = "점수가 일치하는 설문결과가 없습니다.";

	private final SurveyResultJpaRepository surveyResultJpaRepository;

	@Override
	public SurveyResult save(SurveyResult surveyResult) {
		return surveyResultJpaRepository.save(surveyResult);
	}

	@Override
	public SurveyResult findClosestScore(int score) {
		return surveyResultJpaRepository.findClosestScore(score)
			.orElseThrow(() -> new AddictionException(UNKNOWN_SURVEY_RESULT));
	}

	@Override
	public void deleteAllInBatch() {
		surveyResultJpaRepository.deleteAllInBatch();
	}
}
