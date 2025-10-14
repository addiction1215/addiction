package com.addiction.challenge.challenge.controller;

import com.addiction.ControllerTestSupport;
import com.addiction.challenge.challenge.service.response.ChallengeDetailResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ChallengeControllerTest extends ControllerTestSupport {

    @DisplayName("챌린지 상세 조회 API")
    @Test
    @WithMockUser(roles = "USER")
    void 챌린지_상세_조회_API() throws Exception {
        // given
        Long challengeId = 1L;
        ChallengeDetailResponse response = ChallengeDetailResponse.builder()
                .badge("badge_url")
                .title("챌린지 제목")
                .content("챌린지 내용")
                .build();

        given(challengeReadService.findById(challengeId)).willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge/{challengeId}", challengeId)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.badge").value("badge_url"))
                .andExpect(jsonPath("$.data.title").value("챌린지 제목"))
                .andExpect(jsonPath("$.data.content").value("챌린지 내용"));
    }
}
