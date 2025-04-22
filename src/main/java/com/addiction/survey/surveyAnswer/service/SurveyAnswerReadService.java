package com.addiction.survey.surveyAnswer.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.addiction.survey.surveyAnswer.repository.SurveyAnswerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurveyAnswerReadService {

	private final SurveyAnswerRepository surveyAnswerRepository;

	public int findScoreById(int answerId) {
		return surveyAnswerRepository.findById(answerId).getScore();
	}

	public int calculateTotalScore(List<Integer> answerId) {
		return answerId.stream()
			.mapToInt(this::findScoreById)
			.sum();
	}
}
