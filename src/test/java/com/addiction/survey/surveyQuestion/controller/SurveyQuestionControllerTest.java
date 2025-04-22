package com.addiction.survey.surveyQuestion.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import com.addiction.ControllerTestSupport;

public class SurveyQuestionControllerTest extends ControllerTestSupport {

	@DisplayName("설문조사를 조회한다")
	@Test
	@WithMockUser(roles = "USER")
	void 설문조사를_조회한다() throws Exception {
		// given
		// when // then
		mockMvc.perform(
				get("/api/v1/surveyQuestion")
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value("200"))
			.andExpect(jsonPath("$.httpStatus").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"));
	}
}
