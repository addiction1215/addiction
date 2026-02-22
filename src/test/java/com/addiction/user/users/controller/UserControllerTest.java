package com.addiction.user.users.controller;

import com.addiction.ControllerTestSupport;
import com.addiction.user.users.controller.request.UserUpdatePurposeRequest;
import com.addiction.user.users.controller.request.UserUpdateRequest;
import com.addiction.user.users.controller.request.UserUpdateSurveyRequest;
import com.addiction.user.users.entity.enums.Sex;
import com.addiction.user.users.service.response.BenefitResponse;

import static org.mockito.BDDMockito.given;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends ControllerTestSupport {


    @DisplayName("사용자 정보를 수정한다.")
    @Test
    @WithMockUser(roles = "USER")
    void 사용자_정보를_수정한다() throws Exception {
        // given
        UserUpdateRequest request = UserUpdateRequest.builder()
                .sex(Sex.MALE)
                .birthDay("12341234")
                .build();

        // when // then
        mockMvc.perform(
                        patch("/api/v1/user")
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

    @DisplayName("사용자 정보를 수정시 성별은 필수이다.")
    @Test
    @WithMockUser(roles = "USER")
    void 사용자_정보를_수정시_성별은_필수이다() throws Exception {
        // given
        UserUpdateRequest request = UserUpdateRequest.builder()
                .birthDay("12341234")
                .build();

        // when // then
        mockMvc.perform(
                        patch("/api/v1/user")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value("400"))
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("성별은 필수입니다."));
    }

    @DisplayName("사용자 정보를 수정시 생년월일은 필수이다.")
    @Test
    @WithMockUser(roles = "USER")
    void 사용자_정보를_수정시_생년월일은_필수이다() throws Exception {
        // given
        UserUpdateRequest request = UserUpdateRequest.builder()
                .sex(Sex.MALE)
                .build();

        // when // then
        mockMvc.perform(
                        patch("/api/v1/user")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value("400"))
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("생년월일은 필수입니다."));
    }

    @DisplayName("사용자의 설문결과를 저장한다.")
    @Test
    @WithMockUser(roles = "USER")
    void 사용자_설문결과를_저장한다() throws Exception {
        // given
        UserUpdateSurveyRequest userUpdateSurveyRequest = UserUpdateSurveyRequest.builder()
                .answerId(List.of(1L, 2L))
                .purpose("금연 화이팅")
                .cigarettePrice(5000)
                .cigaretteCount(10)
                .build();

        // when // then
        mockMvc.perform(
                        patch("/api/v1/user/survey")
                                .content(objectMapper.writeValueAsString(userUpdateSurveyRequest))
                                .contentType(APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("사용자의 설문결과를 저장할 시 답변ID는 필수이다.")
    @Test
    @WithMockUser(roles = "USER")
    void 사용자_설문결과를_저장시_답변ID는_필수이다() throws Exception {
        // given
        UserUpdateSurveyRequest userUpdateSurveyRequest = UserUpdateSurveyRequest.builder()
                .purpose("금연 화이팅")
                .cigarettePrice(5000)
                .cigaretteCount(10)
                .build();

        // when // then
        mockMvc.perform(
                        patch("/api/v1/user/survey")
                                .content(objectMapper.writeValueAsString(userUpdateSurveyRequest))
                                .contentType(APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value("400"))
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("답변 ID 목록은 필수입니다."));
    }

    @DisplayName("사용자의 설문결과를 저장할 시 금연목표는 필수이다.")
    @Test
    @WithMockUser(roles = "USER")
    void 사용자_설문결과를_저장시_금연목표는_필수이다() throws Exception {
        // given
        UserUpdateSurveyRequest userUpdateSurveyRequest = UserUpdateSurveyRequest.builder()
                .answerId(List.of(1L, 2L))
                .cigarettePrice(5000)
                .cigaretteCount(10)
                .build();

        // when // then
        mockMvc.perform(
                        patch("/api/v1/user/survey")
                                .content(objectMapper.writeValueAsString(userUpdateSurveyRequest))
                                .contentType(APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value("400"))
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("금연 목표는 필수입니다."));
    }

    @DisplayName("사용자의 설문결과를 저장할 시 담배가격은 필수이다.")
    @Test
    @WithMockUser(roles = "USER")
    void 사용자_설문결과를_저장시_담배가격은_필수이다() throws Exception {
        // given
        UserUpdateSurveyRequest userUpdateSurveyRequest = UserUpdateSurveyRequest.builder()
                .answerId(List.of(1L, 2L))
                .purpose("금연 화이팅")
                .cigaretteCount(10)
                .build();

        // when // then
        mockMvc.perform(
                        patch("/api/v1/user/survey")
                                .content(objectMapper.writeValueAsString(userUpdateSurveyRequest))
                                .contentType(APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value("400"))
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("담배 가격은 0원 초과이어야 합니다."));
    }


    @DisplayName("사용자의 금연시작 날짜를 조회한다.")
    @Test
    @WithMockUser(roles = "USER")
    void 사용자의_금연시작_날짜를_조회한다() throws Exception {
        // given
        // when // then
        mockMvc.perform(
                        get("/api/v1/user/startDate")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("사용자의 목표를 조회한다")
    @Test
    @WithMockUser(roles = "USER")
    void 사용자의_목표를_조회한다() throws Exception {
        // given
        // when // then
        mockMvc.perform(
                        get("/api/v1/user/purpose")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("사용자의 목표를 수정한다")
    @Test
    @WithMockUser(roles = "USER")
    void 사용자의_목표를_수정한다() throws Exception {
        // given
        UserUpdatePurposeRequest userUpdatePurposeRequest = UserUpdatePurposeRequest.builder()
                .purpose("금연 화이팅")
                .build();
        // when // then
        mockMvc.perform(
                        patch("/api/v1/user/purpose")
                                .content(objectMapper.writeValueAsString(userUpdatePurposeRequest))
                                .contentType(APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("사용자의 목표를 수정시 목표는 필수이다.")
    @Test
    @WithMockUser(roles = "USER")
    void 사용자의_목표를_수정시_목표는_필수이다() throws Exception {
        // given
        UserUpdatePurposeRequest userUpdatePurposeRequest = UserUpdatePurposeRequest.builder()
                .build();

        // when // then
        mockMvc.perform(
                        patch("/api/v1/user/purpose")
                                .content(objectMapper.writeValueAsString(userUpdatePurposeRequest))
                                .contentType(APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value("400"))
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("금연 목표는 필수입니다."));
    }

    @DisplayName("사용자의 간단한 프로필 정보를 조회한다.")
    @Test
    @WithMockUser(roles = "USER")
    void 사용자의_간단한_프로필_정보를_조회한다() throws Exception {
        // given
        // when // then
        mockMvc.perform(
                        get("/api/v1/user/simple-profile")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("나의 혜택(절약액)을 조회한다.")
    @Test
    @WithMockUser(roles = "USER")
    void 나의_혜택을_조회한다() throws Exception {
        // given
        given(benefitService.findMyBenefit())
                .willReturn(BenefitResponse.createResponse(30L, 150000L, 5000L));

        // when // then
        mockMvc.perform(
                        get("/api/v1/user/benefit")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("회원 탈퇴를 한다.")
    @Test
    @WithMockUser(roles = "USER")
    void 회원_탈퇴를_한다() throws Exception {
        // given
        // when // then
        mockMvc.perform(
                        delete("/api/v1/user")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }
}
