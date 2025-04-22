package com.addiction.survey.surveyQuestion.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.addiction.survey.surveyQuestion.dto.service.response.SurveyQuestionFindListServiceResponse;
import com.addiction.survey.surveyQuestion.dto.service.response.SurveyQuestionFindServiceResponse;
import com.addiction.survey.surveyQuestion.repository.SurveyQuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurveyQuestionReadService {

	private final SurveyQuestionRepository surveyQuestionRepository;

	public SurveyQuestionFindListServiceResponse findAll() {
		return SurveyQuestionFindListServiceResponse.of(
			surveyQuestionRepository.findAll().stream().map(SurveyQuestionFindServiceResponse::of).toList()
		);
	}

}
