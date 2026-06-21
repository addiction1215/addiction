package com.addiction.alertHistory.controller.alertHistory;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import com.addiction.ControllerTestSupport;
import com.addiction.alertHistory.entity.AlertDestinationType;
import com.addiction.alertHistory.entity.AlertHistoryStatus;
import com.addiction.alertHistory.service.alertHistory.response.AlertHistoryResponse;
import com.addiction.global.exception.NotFoundException;
import com.addiction.global.page.request.PageInfoRequest;

class AlertHistoryControllerTest extends ControllerTestSupport {

	@DisplayName("ACTIVE 탭 알림 내역을 가져온다.")
	@Test
	@WithMockUser("USER")
	void getAlertHistory_ACTIVE() throws Exception {
		// given
		PageInfoRequest request = PageInfoRequest.builder()
			.build();

		// when // then
		mockMvc.perform(
				get("/api/v1/alertHistories")
					.param("page", String.valueOf(request.getPage()))
					.param("size", String.valueOf(request.getSize()))
					.param("tabType", "ACTIVE")
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value("200"))
			.andExpect(jsonPath("$.httpStatus").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"));
	}

	@DisplayName("NOTICE 탭 알림 내역을 가져온다.")
	@Test
	@WithMockUser("USER")
	void getAlertHistory_NOTICE() throws Exception {
		// given
		PageInfoRequest request = PageInfoRequest.builder()
			.build();

		// when // then
		mockMvc.perform(
				get("/api/v1/alertHistories")
					.param("page", String.valueOf(request.getPage()))
					.param("size", String.valueOf(request.getSize()))
					.param("tabType", "NOTICE")
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value("200"))
			.andExpect(jsonPath("$.httpStatus").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"));
	}

	@DisplayName("공지사항 단일 알림 내역을 가져온다.")
	@Test
	@WithMockUser("USER")
	void getNoticeAlertHistory() throws Exception {
		// given
		Long id = 1L;

		given(alertHistoryReadService.getNoticeAlertHistory(id))
			.willReturn(AlertHistoryResponse.builder()
				.id(id)
				.alertDescription("공지사항")
				.alertHistoryStatus(AlertHistoryStatus.UNCHECKED)
				.alertDestinationType(AlertDestinationType.NOTICE)
				.alertDestinationInfo("공지사항 상세")
				.createdDate(LocalDateTime.of(2024, 3, 1, 12, 0))
				.build());

		// when // then
		mockMvc.perform(
				get("/api/v1/alertHistories/notices/{id}", id)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value("200"))
			.andExpect(jsonPath("$.httpStatus").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"))
			.andExpect(jsonPath("$.data.id").value(id))
			.andExpect(jsonPath("$.data.alertDestinationType").value("NOTICE"));
	}

	@DisplayName("존재하지 않거나 공지와 연결되지 않은 알림 ID로 공지를 조회하면 404를 반환한다.")
	@Test
	@WithMockUser("USER")
	void getNoticeAlertHistoryNotFound() throws Exception {
		// given
		Long id = 2532L;
		given(alertHistoryReadService.getNoticeAlertHistory(id))
			.willThrow(new NotFoundException("해당 공지사항은 없습니다. id = " + id));

		// when // then
		mockMvc.perform(get("/api/v1/alertHistories/notices/{id}", id).with(csrf()))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.statusCode").value(404))
			.andExpect(jsonPath("$.httpStatus").value("NOT_FOUND"));
	}

	@DisplayName("알림 내역 ID를 받아 알림 내역 상태를 업데이트한다.")
	@Test
	@WithMockUser(roles = "USER")
	void updateAlertHistoryStatus() throws Exception {
		// given
		Long id = 1L;

		// when // then
		mockMvc.perform(
				patch("/api/v1/alertHistories/{id}", id)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value("200"))
			.andExpect(jsonPath("$.httpStatus").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"));
	}
}
