package com.addiction.survey.surveyResultDescription.repository;

import org.springframework.stereotype.Repository;

import com.addiction.survey.surveyResultDescription.entity.SurveyResultDescription;

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
