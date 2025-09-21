package com.addiction.surveyQuestion.response;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.addiction.IntegrationTestSupport;
import com.addiction.survey.surveyAnswer.entity.SurveyAnswer;
import com.addiction.survey.surveyQuestion.dto.service.response.SurveyQuestionFindListServiceResponse;
import com.addiction.survey.surveyQuestion.entity.SurveyQuestion;
import com.addiction.survey.surveyQuestion.service.SurveyQuestionReadService;

public class SurveyQuestionReadServiceTest extends IntegrationTestSupport {

	@Autowired
	private SurveyQuestionReadService surveyQuestionReadService;

	@DisplayName("설문조사를 조회한다.")
	@Test
	void 설문조사를_조회한다() {
		SurveyQuestion surveyQuestion = createSurveyQuestion("현재 흡연 여부를 선택해주세요");
		surveyQuestionRepository.save(surveyQuestion);

		SurveyAnswer surveyAnswer1 = createSurveyAnswer(surveyQuestion, "현재 흡연 중이며, 이제 금연하고 싶어요.", 3);
		SurveyAnswer surveyAnswer2 = createSurveyAnswer(surveyQuestion, "현재 금연 중이며, 계속 유지하고 싶어요.", 0);

		surveyAnswerRepository.save(surveyAnswer1);
		surveyAnswerRepository.save(surveyAnswer2);

		SurveyQuestionFindListServiceResponse response = surveyQuestionReadService.findAllByOrderBySortAsc();

		assertAll(
			() -> assertThat(response.getResponse()).extracting("id", "question")
				.contains(Tuple.tuple(surveyQuestion.getId(), "현재 흡연 여부를 선택해주세요")),
			() -> assertThat(response.getResponse().get(0).getSurveyAnswer()).extracting("answer")
				.containsExactlyInAnyOrder("현재 흡연 중이며, 이제 금연하고 싶어요.", "현재 금연 중이며, 계속 유지하고 싶어요.")
		);
	}
}
