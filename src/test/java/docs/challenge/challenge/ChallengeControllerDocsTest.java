package docs.challenge.challenge;

import com.addiction.challenge.challange.controller.ChallengeController;
import com.addiction.challenge.challange.service.ChallengeReadService;
import com.addiction.challenge.challange.service.response.ChallengeResponse;
import com.addiction.global.page.response.PageCustom;
import com.addiction.global.page.response.PageableCustom;
import docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChallengeControllerDocsTest extends RestDocsSupport {

    private final ChallengeReadService challengeReadService = mock(ChallengeReadService.class);

    @Override
    protected Object initController() {
        return new ChallengeController(challengeReadService);
    }


    @DisplayName("남은 챌린지 리스트 조회 API")
    @Test
    void 남은_챌린지_리스트_조회_API() throws Exception {
        // given
        List<ChallengeResponse> challengeList = List.of(
                ChallengeResponse.builder()
                        .challengeId(1L)
                        .title("7일 연속 금연")
                        .content("7일 동안 연속으로 금연하기")
                        .badge("https://example.com/badge/7days.png")
                        .reward(100)
                        .build(),
                ChallengeResponse.builder()
                        .challengeId(2L)
                        .title("30일 연속 금연")
                        .content("30일 동안 연속으로 금연하기")
                        .badge("https://example.com/badge/30days.png")
                        .reward(100)
                        .build()
        );

        PageableCustom pageableCustom = PageableCustom.builder()
                .currentPage(1)
                .totalPage(5)
                .totalElement(50L)
                .build();

        PageCustom<ChallengeResponse> pageResponse = PageCustom.<ChallengeResponse>builder()
                .content(challengeList)
                .pageInfo(pageableCustom)
                .build();

        given(challengeReadService.getLeftChallengeList(any()))
                .willReturn(pageResponse);

        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge/left")
                                .param("page", "1")
                                .param("size", "12")
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("challenge-get-left-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호 (기본값: 1)").optional(),
                                parameterWithName("size").description("페이지 크기 (기본값: 12)").optional()
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
                                fieldWithPath("data.content[]").type(JsonFieldType.ARRAY)
                                        .description("챌린지 리스트"),
                                fieldWithPath("data.content[].challengeId").type(JsonFieldType.NUMBER)
                                        .description("챌린지 ID"),
                                fieldWithPath("data.content[].title").type(JsonFieldType.STRING)
                                        .description("챌린지 제목"),
                                fieldWithPath("data.content[].content").type(JsonFieldType.STRING)
                                        .description("챌린지 내용"),
                                fieldWithPath("data.content[].badge").type(JsonFieldType.STRING)
                                        .description("배지 이미지 URL"),
                                fieldWithPath("data.content[].reward").type(JsonFieldType.NUMBER)
                                        .description("리워드 점수"),
                                fieldWithPath("data.pageInfo").type(JsonFieldType.OBJECT)
                                        .description("페이지 정보"),
                                fieldWithPath("data.pageInfo.currentPage").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지"),
                                fieldWithPath("data.pageInfo.totalPage").type(JsonFieldType.NUMBER)
                                        .description("전체 페이지 수"),
                                fieldWithPath("data.pageInfo.totalElement").type(JsonFieldType.NUMBER)
                                        .description("전체 요소 수")
                        )
                ));
    }
}
