package docs.challenge;

import com.addiction.challenge.controller.ChallengeController;
import com.addiction.challenge.service.ChallengeReadService;
import com.addiction.challenge.service.ChallengeService;
import docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChallengeControllerDocsTest extends RestDocsSupport {
    private final ChallengeService challengeService = mock(ChallengeService.class);
    private final ChallengeReadService challengeReadService = mock(ChallengeReadService.class);

    @Override
    protected Object initController() {
        return new ChallengeController(challengeReadService, challengeService);
    }

    @DisplayName("챌린지 이력 조회 API")
    @Test
    @WithMockUser(roles = "USER")
    void 유저의_챌린지_이력을_조회한다() throws Exception {

        // when // then
        mockMvc.perform(
                        get("/api/v1/challeng-history")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("challenge-history-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("httpStatus").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data[]").type(JsonFieldType.ARRAY)
                                        .description("응답 데이터"),
                                fieldWithPath("data[].challengeId").type(JsonFieldType.NUMBER)
                                        .description("챌린지 ID"),
                                fieldWithPath("data[].badge").type(JsonFieldType.STRING)
                                        .description("뱃지 URL"),
                                fieldWithPath("data[].title").type(JsonFieldType.STRING)
                                        .description("챌린지 제목")
                        )
                ));
    }
}
