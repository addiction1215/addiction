package com.addiction.alertSetting.controller;

import static org.springframework.http.MediaType.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import com.addiction.ControllerTestSupport;
import com.addiction.alertSetting.controller.request.AlertSettingUpdateRequest;
import com.addiction.alertSetting.entity.enums.AlertType;

public class AlertSettingControllerTest extends ControllerTestSupport {

	@DisplayName("알림 설정을 조회한다.")
	@Test
	@WithMockUser(roles = "USER")
	void 알림_설정을_조회한다() throws Exception {
		// given
		// when // then
		mockMvc.perform(
				get("/api/v1/alert-setting")
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value("200"))
			.andExpect(jsonPath("$.httpStatus").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"));
	}

	@DisplayName("알림 설정을 수정한다.")
	@Test
	@WithMockUser(roles = "USER")
	void 알림_설정을_수정한다() throws Exception {
		// given
		AlertSettingUpdateRequest request = AlertSettingUpdateRequest.builder()
			.all(AlertType.ON)
			.smokingWarning(AlertType.OFF)
			.leaderboardRank(AlertType.ON)
			.challenge(AlertType.ON)
			.report(AlertType.OFF)
			.build();

		// when // then
		mockMvc.perform(
				patch("/api/v1/alert-setting")
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

	@DisplayName("알림 설정 수정시 전체 알림 설정은 필수이다.")
	@Test
	@WithMockUser(roles = "USER")
	void 알림_설정_수정시_전체_알림_설정은_필수이다() throws Exception {
		// given
		AlertSettingUpdateRequest request = AlertSettingUpdateRequest.builder()
			.smokingWarning(AlertType.OFF)
			.leaderboardRank(AlertType.ON)
			.challenge(AlertType.ON)
			.report(AlertType.OFF)
			.build();

		// when // then
		mockMvc.perform(
				patch("/api/v1/alert-setting")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.statusCode").value("400"))
			.andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("전체 알림 설정은 필수입니다."));
	}

	@DisplayName("알림 설정 수정시 흡연 주의 알림 설정은 필수이다.")
	@Test
	@WithMockUser(roles = "USER")
	void 알림_설정_수정시_흡연_주의_알림_설정은_필수이다() throws Exception {
		// given
		AlertSettingUpdateRequest request = AlertSettingUpdateRequest.builder()
			.all(AlertType.ON)
			.leaderboardRank(AlertType.ON)
			.challenge(AlertType.ON)
			.report(AlertType.OFF)
			.build();

		// when // then
		mockMvc.perform(
				patch("/api/v1/alert-setting")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.statusCode").value("400"))
			.andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("흡연 주의 알림 설정은 필수입니다."));
	}

	@DisplayName("알림 설정 수정시 리더보드 순위 알림 설정은 필수이다.")
	@Test
	@WithMockUser(roles = "USER")
	void 알림_설정_수정시_리더보드_순위_알림_설정은_필수이다() throws Exception {
		// given
		AlertSettingUpdateRequest request = AlertSettingUpdateRequest.builder()
			.all(AlertType.ON)
			.smokingWarning(AlertType.OFF)
			.challenge(AlertType.ON)
			.report(AlertType.OFF)
			.build();

		// when // then
		mockMvc.perform(
				patch("/api/v1/alert-setting")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.statusCode").value("400"))
			.andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("리더보드 순위 알림 설정은 필수입니다."));
	}

	@DisplayName("알림 설정 수정시 챌린지 알림 설정은 필수이다.")
	@Test
	@WithMockUser(roles = "USER")
	void 알림_설정_수정시_챌린지_알림_설정은_필수이다() throws Exception {
		// given
		AlertSettingUpdateRequest request = AlertSettingUpdateRequest.builder()
			.all(AlertType.ON)
			.smokingWarning(AlertType.OFF)
			.leaderboardRank(AlertType.ON)
			.report(AlertType.OFF)
			.build();

		// when // then
		mockMvc.perform(
				patch("/api/v1/alert-setting")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.statusCode").value("400"))
			.andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("챌린지 알림 설정은 필수입니다."));
	}

	@DisplayName("알림 설정 수정시 리포트 알림 설정은 필수이다.")
	@Test
	@WithMockUser(roles = "USER")
	void 알림_설정_수정시_리포트_알림_설정은_필수이다() throws Exception {
		// given
		AlertSettingUpdateRequest request = AlertSettingUpdateRequest.builder()
			.all(AlertType.ON)
			.smokingWarning(AlertType.OFF)
			.leaderboardRank(AlertType.ON)
			.challenge(AlertType.ON)
			.build();

		// when // then
		mockMvc.perform(
				patch("/api/v1/alert-setting")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.statusCode").value("400"))
			.andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("리포트 알림 설정은 필수입니다."));
	}
}
