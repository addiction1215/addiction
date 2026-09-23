package com.addiction.smokefree.controller;

import com.addiction.ControllerTestSupport;
import com.addiction.smokefree.service.response.SmokeFreeConfirmationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SmokeFreeConfirmationControllerTest extends ControllerTestSupport {

    @DisplayName("금연일을 확정한다")
    @Test
    @WithMockUser(roles = "USER")
    void confirm() throws Exception {
        given(smokeFreeConfirmationService.confirm(any()))
                .willReturn(SmokeFreeConfirmationResponse.builder().date("20260922").build());

        mockMvc.perform(post("/api/v1/smoke-free-confirmations")
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"date":"20260922"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.date").value("20260922"));
    }
}
