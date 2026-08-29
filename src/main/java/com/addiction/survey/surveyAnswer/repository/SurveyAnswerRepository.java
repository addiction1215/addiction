package com.addiction.survey.surveyAnswer.repository;

import java.util.List;
import java.util.Optional;

import com.addiction.survey.surveyAnswer.entity.SurveyAnswer;

public interface SurveyAnswerRepository {
	SurveyAnswer save(SurveyAnswer surveyAnswer);

	Optional<SurveyAnswer> findById(Long id);

    // 전달받은 선택지 답변들을 찾고, 각 답변이 속한 설문 질문도 함께 채워서 반환
    List<SurveyAnswer> findAllByIdInWithSurveyQuestion(List<Long> ids);

	void deleteAllInBatch();
}
