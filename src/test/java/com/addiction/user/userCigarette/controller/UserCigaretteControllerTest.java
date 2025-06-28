package com.addiction.user.userCigarette.controller;

import static org.springframework.http.MediaType.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import com.addiction.ControllerTestSupport;
import com.addiction.user.userCigarette.controller.request.UserCigaretteChangeRequest;
import com.addiction.user.userCigarette.service.request.ChangeType;

public class UserCigaretteControllerTest extends ControllerTestSupport {

	@DisplayName("금일 핀 담배개수를 조회한다.")
	@Test
	@WithMockUser(roles = "USER")
	void 금일_핀_담배개수를_조회한다() throws Exception {
		// given
		// when // then
		mockMvc.perform(
				get("/api/v1/user/cigarette")
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value("200"))
			.andExpect(jsonPath("$.httpStatus").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"));
	}

	@DisplayName("담배 개수를 수정한다")
	@Test
	@WithMockUser(roles = "USER")
	void 담배_개수를_수정한다() throws Exception {
		// given
		UserCigaretteChangeRequest request = UserCigaretteChangeRequest.builder()
			.changeType(ChangeType.ADD)
			.address("서울시 강남구 역삼동")
			.build();
		// when // then
		mockMvc.perform(
				patch("/api/v1/user/cigarette/change")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value("200"))
			.andExpect(jsonPath("$.httpStatus").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"));
	}
}
