package com.addiction.survey.surveyAnswer;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.addiction.IntegrationTestSupport;
import com.addiction.global.exception.AddictionException;
import com.addiction.survey.surveyAnswer.entity.SurveyAnswer;
import com.addiction.survey.surveyAnswer.service.SurveyAnswerReadService;
import com.addiction.survey.surveyQuestion.entity.SurveyQuestion;

public class SurveyAnswerReadServiceTest extends IntegrationTestSupport {

	@Autowired
	private SurveyAnswerReadService surveyAnswerReadService;

	@DisplayName("설문조사 답변의 점수를 조회한다.")
	@Test
	void 설문조사_답변의_점수를_조회한다() {
		//given
		SurveyQuestion surveyQuestion = createSurveyQuestion("현재 흡연 여부를 선택해주세요");
		surveyQuestionRepository.save(surveyQuestion);

		SurveyAnswer surveyAnswer = createSurveyAnswer(surveyQuestion, "현재 흡연 중이며, 이제 금연하고 싶어요.", 3);

		surveyAnswerRepository.save(surveyAnswer);

		//when, then
		assertThat(surveyAnswerReadService.findScoreById(surveyAnswer.getId())).isEqualTo(3);
	}

	@DisplayName("설문조사 답변의 점수를 조회시 존재하지 않으면 예외가 발생한다.")
	@Test
	void 설문조사_답변의_점수를_조회시_존재하지_않으면_예외가_발생한다() {
		//given
		//when, then
		assertThatThrownBy(() -> surveyAnswerReadService.findScoreById(1L))
			.isInstanceOf(AddictionException.class)
			.hasMessage("존재하지 않는 설문조사 답변입니다.");
	}

	@DisplayName("설문조사 답변의 점수를 총 합산한다.")
	@Test
	void 설문조사_답변의_점수를_총_합산한다() {
		//given
		SurveyQuestion surveyQuestion = createSurveyQuestion("현재 흡연 여부를 선택해주세요");
		surveyQuestionRepository.save(surveyQuestion);

		SurveyAnswer surveyAnswer1 = createSurveyAnswer(surveyQuestion, "현재 흡연 중이며, 이제 금연하고 싶어요.", 3);
		SurveyAnswer surveyAnswer2 = createSurveyAnswer(surveyQuestion, "현재 금연 중이며, 계속 유지하고 싶어요.", 2);

		surveyAnswerRepository.save(surveyAnswer1);
		surveyAnswerRepository.save(surveyAnswer2);

		//when, then
		assertThat(surveyAnswerReadService.calculateTotalScore(List.of(surveyAnswer1.getId(), surveyAnswer2.getId()))).isEqualTo(5);
	}
}
