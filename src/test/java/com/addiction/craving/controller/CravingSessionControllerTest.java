package com.addiction.craving.controller;

import com.addiction.ControllerTestSupport;
import com.addiction.craving.service.response.CravingSessionCompleteResponse;
import com.addiction.craving.service.response.CravingSessionResponse;
import com.addiction.craving.service.response.TodayCravingSummaryResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CravingSessionControllerTest extends ControllerTestSupport {
    @DisplayName("갈망 대응 세션을 시작한다")
    @Test
    @WithMockUser(roles = "USER")
    void start() throws Exception {
        LocalDateTime startedAt = LocalDateTime.of(2026, 9, 22, 21, 0);
        given(cravingSessionService.start()).willReturn(
                CravingSessionResponse.builder()
                        .id(1L)
                        .startedAt(startedAt)
                        .durationSeconds(30)
                        .build()
        );

        mockMvc.perform(post("/api/v1/craving-sessions")
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.durationSeconds").value(30));
    }

    @DisplayName("오늘 완료한 갈망 대응 횟수를 조회한다")
    @Test
    @WithMockUser(roles = "USER")
    void getTodaySummary() throws Exception {
        given(cravingSessionService.getTodaySummary()).willReturn(
                TodayCravingSummaryResponse.of(2)
        );

        mockMvc.perform(get("/api/v1/craving-sessions/today"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.completedCount").value(2));
    }

    @DisplayName("갈망 대응 세션을 완료한다")
    @Test
    @WithMockUser(roles = "USER")
    void complete() throws Exception {
        LocalDateTime completedAt = LocalDateTime.of(2026, 9, 22, 21, 1);
        given(cravingSessionService.complete(1L)).willReturn(
                CravingSessionCompleteResponse.builder()
                        .id(1L)
                        .completedAt(completedAt)
                        .todayCompletedCount(1)
                        .build()
        );

        mockMvc.perform(post("/api/v1/craving-sessions/{sessionId}/complete", 1L)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.todayCompletedCount").value(1));
    }
}
