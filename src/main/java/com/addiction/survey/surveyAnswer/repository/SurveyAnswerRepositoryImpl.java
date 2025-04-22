package com.addiction.survey.surveyAnswer.repository;

import org.springframework.stereotype.Repository;

import com.addiction.survey.surveyAnswer.entity.SurveyAnswer;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SurveyAnswerRepositoryImpl implements SurveyAnswerRepository {

	private final SurveyAnswerJpaRepository surveyAnswerJpaRepository;

	@Override
	public SurveyAnswer save(SurveyAnswer surveyAnswer) {
		return surveyAnswerJpaRepository.save(surveyAnswer);
	}

	@Override
	public void deleteAllInBatch() {
		surveyAnswerJpaRepository.deleteAllInBatch();
	}
}
