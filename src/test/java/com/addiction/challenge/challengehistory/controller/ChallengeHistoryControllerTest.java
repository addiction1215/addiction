package com.addiction.challenge.challengehistory.controller;

import com.addiction.ControllerTestSupport;
import com.addiction.challenge.challengehistory.service.response.ChallengeHistoryUserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ChallengeHistoryControllerTest extends ControllerTestSupport {

    @DisplayName("유저의 챌린지 이력을 조회한다.")
    @Test
    @WithMockUser(roles = "USER")
    void findByUserId() throws Exception {
        // given
        ChallengeHistoryUserResponse response1 = ChallengeHistoryUserResponse.builder()
                .challengeId(1L)
                .badge("badge_url_1")
                .title("챌린지 1")
                .build();

        ChallengeHistoryUserResponse response2 = ChallengeHistoryUserResponse.builder()
                .challengeId(2L)
                .badge("badge_url_2")
                .title("챌린지 2")
                .build();

        List<ChallengeHistoryUserResponse> response = List.of(response1, response2);

        given(challengeHistoryReadService.findByUserId()).willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/challeng-history")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2));
    }
}
