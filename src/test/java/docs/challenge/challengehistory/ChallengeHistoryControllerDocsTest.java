package docs.challenge.challengehistory;

import com.addiction.challenge.challengehistory.controller.ChallengeHistoryController;
import com.addiction.challenge.challengehistory.service.ChallengeHistoryReadService;
import com.addiction.challenge.challengehistory.service.ChallengeHistoryService;
import com.addiction.challenge.challengehistory.service.response.ChallengeHistoryResponse;
import com.addiction.common.enums.ChallengeStatus;
import com.addiction.global.page.response.PageCustom;
import com.addiction.global.page.response.PageableCustom;
import docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.Arrays;
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

public class ChallengeHistoryControllerDocsTest extends RestDocsSupport {

    private final ChallengeHistoryReadService challengeHistoryReadService = mock(ChallengeHistoryReadService.class);
    private final ChallengeHistoryService challengeHistoryService = mock(ChallengeHistoryService.class);

    @Override
    protected Object initController() {
        return new ChallengeHistoryController(challengeHistoryReadService);
    }

    @DisplayName("진행중인 챌린지 조회 API")
    @Test
    void 진행중인_챌린지_조회_API() throws Exception {
        // given
        ChallengeHistoryResponse response = ChallengeHistoryResponse.builder()
                .challengeHistoryId(1L)
                .title("7일 연속 금연")
                .content("7일 동안 연속으로 금연하기")
                .badge("https://example.com/badge/7days.png")
                .status(ChallengeStatus.PROGRESSING)
                .reward(100)
                .build();

        given(challengeHistoryReadService.getProgressingChallenge())
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge-history/progressing")
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("challenge-history-get-progressing",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("httpStatus").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.challengeHistoryId").type(JsonFieldType.NUMBER)
                                        .description("챌린지 이력 ID"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING)
                                        .description("챌린지 제목"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING)
                                        .description("챌린지 내용"),
                                fieldWithPath("data.badge").type(JsonFieldType.STRING)
                                        .description("배지 이미지 URL"),
                                fieldWithPath("data.reward").type(JsonFieldType.NUMBER)
                                        .description("리워드 점수"),
                                fieldWithPath("data.status").type(JsonFieldType.STRING)
                                        .description("챌린지 상태: " + Arrays.toString(ChallengeStatus.values()))
                        )
                ));
    }

    @DisplayName("완료된 챌린지 리스트 조회 API")
    @Test
    void 완료된_챌린지_리스트_조회_API() throws Exception {
        // given
        List<ChallengeHistoryResponse> challengeList = List.of(
                ChallengeHistoryResponse.builder()
                        .challengeHistoryId(3L)
                        .title("첫 금연일 달성")
                        .content("첫날 금연 성공하기")
                        .badge("https://example.com/badge/first.png")
                        .status(ChallengeStatus.COMPLETED)
                        .reward(100)
                        .build(),
                ChallengeHistoryResponse.builder()
                        .challengeHistoryId(4L)
                        .title("3일 연속 금연")
                        .content("3일 동안 연속으로 금연하기")
                        .badge("https://example.com/badge/3days.png")
                        .status(ChallengeStatus.COMPLETED)
                        .reward(100)
                        .build()
        );

        PageableCustom pageableCustom = PageableCustom.builder()
                .currentPage(1)
                .totalPage(3)
                .totalElement(30L)
                .build();

        PageCustom<ChallengeHistoryResponse> pageResponse = PageCustom.<ChallengeHistoryResponse>builder()
                .content(challengeList)
                .pageInfo(pageableCustom)
                .build();

        given(challengeHistoryReadService.getFinishedChallengeList(any()))
                .willReturn(pageResponse);

        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge-history/finished")
                                .param("page", "1")
                                .param("size", "12")
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("challenge-history-get-finished-list",
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
                                fieldWithPath("data.content[].challengeHistoryId").type(JsonFieldType.NUMBER)
                                        .description("챌린지 이력 ID"),
                                fieldWithPath("data.content[].title").type(JsonFieldType.STRING)
                                        .description("챌린지 제목"),
                                fieldWithPath("data.content[].content").type(JsonFieldType.STRING)
                                        .description("챌린지 내용"),
                                fieldWithPath("data.content[].badge").type(JsonFieldType.STRING)
                                        .description("배지 이미지 URL"),
                                fieldWithPath("data.content[].status").type(JsonFieldType.STRING)
                                        .description("챌린지 상태: " + Arrays.toString(ChallengeStatus.values())),
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
