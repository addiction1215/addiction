package com.addiction.survey.surveyAnswer.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.addiction.global.exception.AddictionException;
import com.addiction.survey.surveyAnswer.entity.SurveyAnswer;
import com.addiction.survey.surveyAnswer.repository.SurveyAnswerRepository;
import com.addiction.survey.surveyAnswer.service.SurveyAnswerReadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurveyAnswerReadServiceImpl implements SurveyAnswerReadService {

	private static final String UNKNOWN_SURVEY_ANSWER = "존재하지 않는 설문조사 답변입니다.";

	private final SurveyAnswerRepository surveyAnswerRepository;

	@Override
	public SurveyAnswer findById(int answerId) {
		return surveyAnswerRepository.findById(answerId)
			.orElseThrow(() -> new AddictionException(UNKNOWN_SURVEY_ANSWER));
	}

	@Override
	public int findScoreById(int answerId) {
		return findById(answerId).getScore();
	}

	@Override
	public int calculateTotalScore(List<Integer> answerId) {
		return answerId.stream()
			.mapToInt(this::findScoreById)
			.sum();
	}
}
