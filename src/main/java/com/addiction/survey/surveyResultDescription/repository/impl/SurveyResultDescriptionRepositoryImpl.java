package com.addiction.survey.surveyResultDescription.repository.impl;

import org.springframework.stereotype.Repository;

import com.addiction.survey.surveyResultDescription.entity.SurveyResultDescription;
import com.addiction.survey.surveyResultDescription.repository.SurveyResultDescriptionJpaRepository;
import com.addiction.survey.surveyResultDescription.repository.SurveyResultDescriptionRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SurveyResultDescriptionRepositoryImpl implements SurveyResultDescriptionRepository {

	private final SurveyResultDescriptionJpaRepository surveyResultDescriptionJpaRepository;

	@Override
	public SurveyResultDescription save(SurveyResultDescription surveyResultDescription) {
		return surveyResultDescriptionJpaRepository.save(surveyResultDescription);
	}

	@Override
	public void deleteAllInBatch() {
		surveyResultDescriptionJpaRepository.deleteAllInBatch();
	}
}
