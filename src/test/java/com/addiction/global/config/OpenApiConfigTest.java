package com.addiction.global.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.addiction.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class OpenApiConfigTest extends IntegrationTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("사용자 API OpenAPI 명세를 제공한다.")
    @Test
    void 사용자_API_OpenAPI_명세를_제공한다() throws Exception {
        mockMvc.perform(get("/v3/api-docs/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title").value("QuitMate User API"))
                .andExpect(jsonPath("$.paths['/api/v1/user/smoking-tendency']").exists())
                .andExpect(jsonPath("$.paths['/api/v1/user/cumulative-change']").exists())
                .andExpect(jsonPath("$.paths['/api/v1/challenge']").doesNotExist())
                .andExpect(jsonPath("$.components.securitySchemes.bearerAuth.scheme").value("bearer"))
                .andExpect(jsonPath("$.components.schemas.UserUpdateSurveyRequest.properties.cigarettePrice.description")
                        .value("하루 평균 담배 지출액(원)"))
                .andExpect(jsonPath("$.components.schemas.UserSmokingTendencyResponse.properties.scoreChange.description")
                        .value("현재 QuitMate 점수 - 직전 QuitMate 점수. 비교할 이전 설문이 없으면 null입니다."))
                .andExpect(jsonPath("$.components.schemas.UserSmokingTendencyResponse.properties.comparisonStatus.enum").isArray());
    }

    @DisplayName("기본 OpenAPI 명세도 사용자 API로 제한한다.")
    @Test
    void 기본_OpenAPI_명세도_사용자_API로_제한한다() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/api/v1/user/smoking-tendency']").exists())
                .andExpect(jsonPath("$.paths['/api/v1/challenge']").doesNotExist());
    }

    @DisplayName("Swagger UI에 인증 없이 접근할 수 있다.")
    @Test
    void Swagger_UI에_인증_없이_접근할_수_있다() throws Exception {
        mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().is3xxRedirection());
    }
}
