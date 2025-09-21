package com.addiction.survey.surveyResult.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.addiction.IntegrationTestSupport;
import com.addiction.survey.surveyResult.entity.SurveyResult;

public class SurveyResultReadServiceTest extends IntegrationTestSupport {

	@Autowired
	private SurveyResultReadService surveyResultReadService;

	@DisplayName("점수를 입력받아 해당되는 설문결과를 가져온다.")
	@Test
	void 점수를_입력받아_해당되는_설문결과를_가져온다() {
		//given
		SurveyResult surveyResult1 = createSurveyResult("라이트 스모커", 0);
		SurveyResult surveyResult2 = createSurveyResult("미디엄 스모커", 7);
		SurveyResult surveyResult3 = createSurveyResult("헤비 스모커", 13);

		surveyResultRepository.save(surveyResult1);
		surveyResultRepository.save(surveyResult2);
		surveyResultRepository.save(surveyResult3);

		//when, then
		assertAll(
			() -> assertThat(surveyResultReadService.findClosestScore(0).getTitle()).isEqualTo("라이트 스모커"),
			() -> assertThat(surveyResultReadService.findClosestScore(7).getTitle()).isEqualTo("미디엄 스모커"),
			() -> assertThat(surveyResultReadService.findClosestScore(13).getTitle()).isEqualTo("헤비 스모커")
		);
	}
}
