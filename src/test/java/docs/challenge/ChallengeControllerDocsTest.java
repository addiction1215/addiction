package docs.challenge;

import com.addiction.challenge.challenge.controller.ChallengeController;
import com.addiction.challenge.challenge.service.ChallengeReadService;
import com.addiction.challenge.challenge.service.response.ChallengeDetailResponse;
import docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChallengeControllerDocsTest extends RestDocsSupport {

    private final ChallengeReadService challengeReadService = mock(ChallengeReadService.class);

    @Override
    protected Object initController() {
        return new ChallengeController(challengeReadService);
    }

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
                .andDo(document("challenge-detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("challengeId").description("챌린지 ID")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("httpStatus").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.badge").type(JsonFieldType.STRING)
                                        .description("뱃지 URL"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING)
                                        .description("챌린지 제목"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING)
                                        .description("챌린지 내용")
                        )
                ));
    }
}
