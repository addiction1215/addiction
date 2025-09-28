package com.addiction.user.userCigaretteHistory.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import com.addiction.ControllerTestSupport;

public class UserCigaretteHistoryControllerTest extends ControllerTestSupport {

	@DisplayName("월별 캘린더 흡연기록을 조회한다")
	@Test
	@WithMockUser(roles = "USER")
	void 월별_캘린더_흡연기록_조회() throws Exception {
		mockMvc.perform(
				get("/api/v1/user/cigarette-history/calendar")
					.param("month", "2024-06")
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value("200"))
			.andExpect(jsonPath("$.httpStatus").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"));
	}

	@DisplayName("특정 날짜의 흡연기록을 조회한다")
	@Test
	@WithMockUser(roles = "USER")
	void 특정_날짜_흡연기록_조회() throws Exception {
		mockMvc.perform(
				get("/api/v1/user/cigarette-history/history")
					.param("date", "2024-06-01")
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value("200"))
			.andExpect(jsonPath("$.httpStatus").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"));
	}

	@DisplayName("기간별 흡연기록 그래프를 조회한다")
	@Test
	@WithMockUser(roles = "USER")
	void 기간별_흡연기록_그래프_조회() throws Exception {
		mockMvc.perform(
				get("/api/v1/user/cigarette-history/graph")
					.param("periodType", "WEEKLY")
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value("200"))
			.andExpect(jsonPath("$.httpStatus").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"));
	}

    @DisplayName("유저의 마지막 흡연 기록을 조회한다.")
    @Test
    @WithMockUser(roles = "USER")
    void 유저의_마지막_흡연_기록을_조회한다() throws Exception {
        mockMvc.perform(
                        get("/api/v1/user/cigarette-history/lastest")
                                .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.statusCode").value("200"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }
}
