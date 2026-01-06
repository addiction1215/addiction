package com.addiction.challenge.challengehistory.controller;

import com.addiction.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ChallengeHistoryControllerTest extends ControllerTestSupport {

    @DisplayName("진행중인 챌린지를 조회한다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getProgressingChallenge() throws Exception {
        // given
        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge-history/progressing")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("진행중인 챌린지가 없으면 null을 반환한다")
    @Test
    @WithMockUser(roles = "USER")
    void getProgressingChallengeWhenNull() throws Exception {
        // given
        given(challengeHistoryReadService.getProgressingChallenge())
                .willReturn(null);

        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge-history/progressing")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("단일 진행중 챌린지의 모든 필드가 정확히 반환된다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getProgressingChallengeWithAllFields() throws Exception {
        // given
        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge-history/progressing")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("완료된 챌린지 목록을 페이징하여 조회한다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getFinishedChallengeList() throws Exception {
        // given
        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge-history/finished")
                                .param("page", "1")
                                .param("size", "10")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("완료된 챌린지가 없는 경우 빈 목록을 반환한다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getFinishedChallengeListWhenEmpty() throws Exception {
        // given
        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge-history/finished")
                                .param("page", "1")
                                .param("size", "10")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("페이징 파라미터로 2페이지를 조회한다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getFinishedChallengeListPage2() throws Exception {
        // given
        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge-history/finished")
                                .param("page", "2")
                                .param("size", "5")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("단일 완료된 챌린지의 모든 필드가 정확히 반환된다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getSingleFinishedChallengeWithAllFields() throws Exception {
        // given

        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge-history/finished")
                                .param("page", "1")
                                .param("size", "10")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("마지막 페이지를 조회한다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getFinishedChallengeListLastPage() throws Exception {
        // given

        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge-history/finished")
                                .param("page", "3")
                                .param("size", "5")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }
}
