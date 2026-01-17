package com.addiction.challenge.challange.controller;

import com.addiction.ControllerTestSupport;
import com.addiction.challenge.challange.service.response.ChallengeResponse;
import com.addiction.global.page.response.PageCustom;
import com.addiction.global.page.response.PageableCustom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ChallengeControllerTest extends ControllerTestSupport {

    @DisplayName("남은 챌린지 목록을 페이징하여 조회한다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getLeftChallengeList() throws Exception {
        // given
        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge/left")
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

    @DisplayName("남은 챌린지가 없는 경우 빈 목록을 반환한다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getLeftChallengeListWhenEmpty() throws Exception {
        // given

        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge/left")
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
    void getLeftChallengeListPage2() throws Exception {
        // given
        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge/left")
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

    @DisplayName("단일 챌린지의 모든 필드가 정확히 반환된다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getSingleChallengeWithAllFields() throws Exception {
        // given
        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge/left")
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

    @DisplayName("페이지 크기를 다르게 설정하여 조회한다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getLeftChallengeListWithDifferentPageSize() throws Exception {
        // given
        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge/left")
                                .param("page", "1")
                                .param("size", "3")
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
    void getLeftChallengeListLastPage() throws Exception {
        // given
        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge/left")
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
