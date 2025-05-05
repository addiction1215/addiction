package com.addiction.survey.surveyAnswer.repository.impl;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.addiction.global.exception.AddictionException;
import com.addiction.survey.surveyAnswer.entity.SurveyAnswer;
import com.addiction.survey.surveyAnswer.repository.SurveyAnswerJpaRepository;
import com.addiction.survey.surveyAnswer.repository.SurveyAnswerRepository;

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
	public Optional<SurveyAnswer> findById(int id) {
		return surveyAnswerJpaRepository.findById(id);
	}

	@Override
	public void deleteAllInBatch() {
		surveyAnswerJpaRepository.deleteAllInBatch();
	}
}
