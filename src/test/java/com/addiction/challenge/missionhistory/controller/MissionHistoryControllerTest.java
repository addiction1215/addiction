package com.addiction.challenge.missionhistory.controller;

import com.addiction.ControllerTestSupport;
import com.addiction.challenge.missionhistory.controller.request.MissionSubmitRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MissionHistoryControllerTest extends ControllerTestSupport {

    @DisplayName("미션 진행 상황을 조회한다")
    @Test
    @WithMockUser(roles = "USER")
    void getMissionProgress() throws Exception {
        // given
        Long challengeHistoryId = 1L;
        // when // then
        mockMvc.perform(
                        get("/api/v1/mission-history/progress/{challengeHistoryId}", challengeHistoryId)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("미션 상세 정보를 조회한다")
    @Test
    @WithMockUser(roles = "USER")
    void getMissionDetail() throws Exception {
        // given
        Long missionHistoryId = 101L;
        // when // then
        mockMvc.perform(
                        get("/api/v1/mission-history/detail/{missionHistoryId}", missionHistoryId)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("GPS 미션을 중간 제출한다")
    @Test
    @WithMockUser(roles = "USER")
    void submitMissionGps() throws Exception {
        // given
        MissionSubmitRequest request = MissionSubmitRequest.builder()
                .missionHistoryId(101L)
                .address("서울시 강남구 테헤란로")
                .build();
        // when // then
        mockMvc.perform(
                        post("/api/v1/mission-history/submit")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("사진 미션을 중간 제출한다")
    @Test
    @WithMockUser(roles = "USER")
    void submitMissionPhoto() throws Exception {
        // given
        MissionSubmitRequest request = MissionSubmitRequest.builder()
                .missionHistoryId(102L)
                .photoUrl("https://example.com/photo1.jpg")
                .build();
        // when // then
        mockMvc.perform(
                        post("/api/v1/mission-history/submit")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("흡연 참기 미션을 중간 제출한다")
    @Test
    @WithMockUser(roles = "USER")
    void submitMissionAbstinence() throws Exception {
        // given
        MissionSubmitRequest request = MissionSubmitRequest.builder()
                .missionHistoryId(103L)
                .time(3600)
                .build();
        // when // then
        mockMvc.perform(
                        post("/api/v1/mission-history/submit")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("미션을 최종 제출한다")
    @Test
    @WithMockUser(roles = "USER")
    void completeMission() throws Exception {
        // given
        Long missionHistoryId = 101L;
        // when // then
        mockMvc.perform(
                        patch("/api/v1/mission-history/complete/{missionHistoryId}", missionHistoryId)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }
}
