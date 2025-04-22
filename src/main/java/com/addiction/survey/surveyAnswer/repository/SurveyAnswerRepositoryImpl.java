package com.addiction.survey.surveyAnswer.repository;

import org.springframework.stereotype.Repository;

import com.addiction.global.exception.AddictionException;
import com.addiction.survey.surveyAnswer.entity.SurveyAnswer;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SurveyAnswerRepositoryImpl implements SurveyAnswerRepository {

	private static final String UNKNOWN_SURVEY_ANSWER = "존재하지 않는 설문조사 답변입니다.";

	private final SurveyAnswerJpaRepository surveyAnswerJpaRepository;

	@Override
	public SurveyAnswer save(SurveyAnswer surveyAnswer) {
		return surveyAnswerJpaRepository.save(surveyAnswer);
	}

	@Override
	public SurveyAnswer findById(int id) {
		return surveyAnswerJpaRepository.findById(id)
			.orElseThrow(() -> new AddictionException(UNKNOWN_SURVEY_ANSWER));
	}

	@Override
	public void deleteAllInBatch() {
		surveyAnswerJpaRepository.deleteAllInBatch();
	}
}
