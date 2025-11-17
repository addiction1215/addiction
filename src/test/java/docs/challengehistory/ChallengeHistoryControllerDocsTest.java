package docs.challengehistory;

import com.addiction.challenge.challengehistory.controller.ChallengeHistoryController;
import com.addiction.challenge.challengehistory.service.ChallengeHistoryReadService;
import com.addiction.challenge.challengehistory.service.response.ChallengeHistoryUserResponse;
import docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChallengeHistoryControllerDocsTest extends RestDocsSupport {

    private final ChallengeHistoryReadService challengeHistoryReadService = mock(ChallengeHistoryReadService.class);

    @Override
    protected Object initController() {
        return new ChallengeHistoryController(challengeHistoryReadService);
    }

    @DisplayName("챌린지 이력 조회 API")
    @Test
    @WithMockUser(roles = "USER")
    void 유저의_챌린지_이력을_조회한다() throws Exception {
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

        given(challengeHistoryReadService.findByUserId())
                .willReturn(response);

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
