package com.addiction.survey.surveyQuestion.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.addiction.survey.surveyQuestion.dto.repository.response.SurveyQuestionDto;
import com.addiction.survey.surveyQuestion.entity.SurveyQuestion;

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
	public List<SurveyQuestion> findAll() {
		return surveyQuestionJpaRepository.findAll();
	}

	@Override
	public void deleteAllInBatch() {
		surveyQuestionJpaRepository.deleteAllInBatch();
	}
}
