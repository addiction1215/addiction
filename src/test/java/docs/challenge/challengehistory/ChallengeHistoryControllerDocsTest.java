package docs.challenge.challengehistory;

import com.addiction.challenge.challengehistory.controller.ChallengeHistoryController;
import com.addiction.challenge.challengehistory.controller.request.ChallengeCancelRequest;
import com.addiction.challenge.challengehistory.controller.request.ChallengeJoinRequest;
import com.addiction.challenge.challengehistory.entity.ChallengeStatus;
import com.addiction.challenge.challengehistory.service.ChallengeHistoryReadService;
import com.addiction.challenge.challengehistory.service.ChallengeHistoryService;
import com.addiction.challenge.challengehistory.service.response.ChallengeCancelResponse;
import com.addiction.challenge.challengehistory.service.response.ChallengeHistoryResponse;
import com.addiction.challenge.challengehistory.service.response.ChallengeJoinResponse;
import com.addiction.challenge.challengehistory.service.response.FinishedChallengeHistoryResponse;
import com.addiction.challenge.missionhistory.entity.MissionStatus;
import com.addiction.global.page.response.PageCustom;
import com.addiction.global.page.response.PageableCustom;
import docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChallengeHistoryControllerDocsTest extends RestDocsSupport {

    private final ChallengeHistoryReadService challengeHistoryReadService = mock(ChallengeHistoryReadService.class);
    private final ChallengeHistoryService challengeHistoryService = mock(ChallengeHistoryService.class);

    @Override
    protected Object initController() {
        return new ChallengeHistoryController(challengeHistoryReadService, challengeHistoryService);
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
                .progress(60)
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
                                fieldWithPath("data.progress").type(JsonFieldType.NUMBER)
                                        .description("미션 진행률 (0~100)"),
                                fieldWithPath("data.status").type(JsonFieldType.STRING)
                                        .description("챌린지 상태: " + Arrays.toString(ChallengeStatus.values()))
                        )
                ));
    }

    @DisplayName("완료된 챌린지 리스트 조회 API")
    @Test
    void 완료된_챌린지_리스트_조회_API() throws Exception {
        // given
        List<FinishedChallengeHistoryResponse> challengeList = List.of(
                FinishedChallengeHistoryResponse.builder()
                        .challengeHistoryId(3L)
                        .title("첫 금연일 달성")
                        .content("첫날 금연 성공하기")
                        .badge("https://example.com/badge/first.png")
                        .status(ChallengeStatus.COMPLETED)
                        .reward(100)
                        .build(),
                FinishedChallengeHistoryResponse.builder()
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

        PageCustom<FinishedChallengeHistoryResponse> pageResponse = PageCustom.<FinishedChallengeHistoryResponse>builder()
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
                                fieldWithPath("data.content[].reward").type(JsonFieldType.NUMBER)
                                        .description("리워드 점수"),
                                fieldWithPath("data.content[].status").type(JsonFieldType.STRING)
                                        .description("챌린지 상태: " + Arrays.toString(ChallengeStatus.values())),
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

    @DisplayName("챌린지 참여 API")
    @Test
    void 챌린지_참여_API() throws Exception {
        // given
        ChallengeJoinRequest request = ChallengeJoinRequest.builder()
                .challengeId(1L)
                .build();

        ChallengeJoinResponse.ChallengeHistoryInfo challengeHistoryInfo = ChallengeJoinResponse.ChallengeHistoryInfo.builder()
                .id(1L)
                .challengeId(1L)
                .challengeTitle("30일 금연 챌린지")
                .challengeContent("30일 동안 금연을 실천하는 챌린지입니다.")
                .status(ChallengeStatus.PROGRESSING)
                .createdDate(LocalDateTime.of(2025, 1, 6, 10, 0, 0))
                .build();

        List<ChallengeJoinResponse.MissionHistoryInfo> missionHistoryInfos = Arrays.asList(
                ChallengeJoinResponse.MissionHistoryInfo.builder()
                        .id(1L)
                        .missionId(1L)
                        .missionTitle("하루 금연하기")
                        .status(MissionStatus.PROGRESSING)
                        .createdDate(LocalDateTime.of(2025, 1, 6, 10, 0, 0))
                        .build(),
                ChallengeJoinResponse.MissionHistoryInfo.builder()
                        .id(2L)
                        .missionId(2L)
                        .missionTitle("일주일 금연하기")
                        .status(MissionStatus.PROGRESSING)
                        .createdDate(LocalDateTime.of(2025, 1, 6, 10, 0, 0))
                        .build()
        );

        ChallengeJoinResponse response = ChallengeJoinResponse.builder()
                .challengeHistory(challengeHistoryInfo)
                .missionHistories(missionHistoryInfos)
                .build();

        given(challengeHistoryService.joinChallenge(any(ChallengeJoinRequest.class)))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        post("/api/v1/challenge-history/join")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("challenge-history-join",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("challengeId").type(JsonFieldType.NUMBER)
                                        .description("참여할 챌린지 ID")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
                                        .description("응답 코드"),
                                fieldWithPath("httpStatus").type(JsonFieldType.STRING)
                                        .description("HTTP 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.challengeHistory").type(JsonFieldType.OBJECT)
                                        .description("챌린지 이력 정보"),
                                fieldWithPath("data.challengeHistory.id").type(JsonFieldType.NUMBER)
                                        .description("챌린지 이력 ID"),
                                fieldWithPath("data.challengeHistory.challengeId").type(JsonFieldType.NUMBER)
                                        .description("챌린지 ID"),
                                fieldWithPath("data.challengeHistory.challengeTitle").type(JsonFieldType.STRING)
                                        .description("챌린지 제목"),
                                fieldWithPath("data.challengeHistory.challengeContent").type(JsonFieldType.STRING)
                                        .description("챌린지 내용"),
                                fieldWithPath("data.challengeHistory.status").type(JsonFieldType.STRING)
                                        .description("챌린지 상태: " + Arrays.toString(ChallengeStatus.values())),
                                fieldWithPath("data.challengeHistory.createdDate").type(JsonFieldType.STRING)
                                        .description("챌린지 이력 생성일"),
                                fieldWithPath("data.missionHistories").type(JsonFieldType.ARRAY)
                                        .description("미션 이력 목록"),
                                fieldWithPath("data.missionHistories[].id").type(JsonFieldType.NUMBER)
                                        .description("미션 이력 ID"),
                                fieldWithPath("data.missionHistories[].missionId").type(JsonFieldType.NUMBER)
                                        .description("미션 ID"),
                                fieldWithPath("data.missionHistories[].missionTitle").type(JsonFieldType.STRING)
                                        .description("미션 제목"),
                                fieldWithPath("data.missionHistories[].status").type(JsonFieldType.STRING)
                                        .description("미션 상태: " + Arrays.toString(MissionStatus.values())),
                                fieldWithPath("data.missionHistories[].createdDate").type(JsonFieldType.STRING)
                                        .description("미션 이력 생성일")
                        )
                ));
    }

    @DisplayName("챌린지 포기 API")
    @Test
    void 챌린지_포기_API() throws Exception {
        // given
        ChallengeCancelRequest request = ChallengeCancelRequest.builder()
                .challengeHistoryId(1L)
                .build();

        ChallengeCancelResponse response = ChallengeCancelResponse.builder()
                .challengeHistoryId(1L)
                .build();

        given(challengeHistoryService.cancelChallenge(any(ChallengeCancelRequest.class)))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        patch("/api/v1/challenge-history/cancel")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("challenge-history-cancel",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("challengeHistoryId").type(JsonFieldType.NUMBER)
                                        .description("포기할 챌린지 이력 ID")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
                                        .description("응답 코드"),
                                fieldWithPath("httpStatus").type(JsonFieldType.STRING)
                                        .description("HTTP 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.challengeHistoryId").type(JsonFieldType.NUMBER)
                                        .description("포기된 챌린지 이력 ID")
                        )
                ));
    }

}
