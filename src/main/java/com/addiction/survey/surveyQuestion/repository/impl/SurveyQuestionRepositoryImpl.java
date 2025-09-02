package com.addiction.survey.surveyQuestion.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.addiction.survey.surveyQuestion.entity.SurveyQuestion;
import com.addiction.survey.surveyQuestion.repository.SurveyQuestionJpaRepository;
import com.addiction.survey.surveyQuestion.repository.SurveyQuestionRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SurveyQuestionRepositoryImpl implements SurveyQuestionRepository {

	private final SurveyQuestionJpaRepository surveyQuestionJpaRepository;

	@Override
	public SurveyQuestion save(SurveyQuestion surveyQuestion) {
		return surveyQuestionJpaRepository.save(surveyQuestion);
	}

	@Override
	public List<SurveyQuestion> findAllByOrderBySortAsc() {
		return surveyQuestionJpaRepository.findAllByOrderBySortAsc();
	}

	@Override
	public void deleteAllInBatch() {
		surveyQuestionJpaRepository.deleteAllInBatch();
	}
}
