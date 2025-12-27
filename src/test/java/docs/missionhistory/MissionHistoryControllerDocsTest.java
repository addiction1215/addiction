package docs.missionhistory;

import com.addiction.common.enums.MissionCategoryStatus;
import com.addiction.common.enums.MissionStatus;
import com.addiction.missionhistory.controller.MissionHistoryController;
import com.addiction.missionhistory.service.MissionHistoryReadService;
import com.addiction.missionhistory.service.response.MissionHistoryResponse;
import com.addiction.missionhistory.service.response.MissionProgressResponse;
import docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MissionHistoryControllerDocsTest extends RestDocsSupport {

    private final MissionHistoryReadService missionHistoryReadService = mock(MissionHistoryReadService.class);

    @Override
    protected Object initController() {
        return new MissionHistoryController(missionHistoryReadService);
    }

    @DisplayName("미션 진행 상황 조회 API")
    @Test
    void 미션_진행_상황_조회_API() throws Exception {
        // given
        List<MissionHistoryResponse> missionItems = List.of(
                MissionHistoryResponse.builder()
                        .missionHistoryId(101L)
                        .missionId(1L)
                        .missionTitle("평소 흡연 루트 피해보기")
                        .missionContent("평소 다니는 흡연 루트를 피해서 이동하기")
                        .category(MissionCategoryStatus.LOCATION)
                        .reward(50)
                        .status(MissionStatus.COMPLETED)
                        .build(),
                MissionHistoryResponse.builder()
                        .missionHistoryId(102L)
                        .missionId(2L)
                        .missionTitle("평소 흡연 시간대 산책하기")
                        .missionContent("평소 흡연하는 시간대에 산책해보기")
                        .category(MissionCategoryStatus.REPLACE_ACTION)
                        .reward(70)
                        .status(MissionStatus.COMPLETED)
                        .build(),
                MissionHistoryResponse.builder()
                        .missionHistoryId(103L)
                        .missionId(3L)
                        .missionTitle("5분간 참기")
                        .missionContent("흡연 욕구가 생길 때 5분간 참아보기")
                        .category(MissionCategoryStatus.HOLD)
                        .reward(30)
                        .status(MissionStatus.PROGRESSING)
                        .build()
        );

        MissionProgressResponse response = MissionProgressResponse.builder()
                .challengeHistoryId(1L)
                .challengeId(1L)
                .challengeTitle("유혹 피하기")
                .totalMissionCount(5)
                .completedMissionCount(2)
                .totalEarnedReward(120)
                .totalPossibleReward(300)
                .missions(missionItems)
                .build();

        given(missionHistoryReadService.getMissionProgress(1L))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/mission-history/progress/{challengeHistoryId}", 1L)
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("mission-history-get-progress",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("challengeHistoryId").description("챌린지 이력 ID")
                        ),
                        responseFields(
                                fieldWithPath("challengeHistoryId").type(JsonFieldType.NUMBER)
                                        .description("챌린지 이력 ID"),
                                fieldWithPath("challengeId").type(JsonFieldType.NUMBER)
                                        .description("챌린지 ID"),
                                fieldWithPath("challengeTitle").type(JsonFieldType.STRING)
                                        .description("챌린지 제목"),
                                fieldWithPath("totalMissionCount").type(JsonFieldType.NUMBER)
                                        .description("전체 미션 개수"),
                                fieldWithPath("completedMissionCount").type(JsonFieldType.NUMBER)
                                        .description("완료한 미션 개수"),
                                fieldWithPath("totalEarnedReward").type(JsonFieldType.NUMBER)
                                        .description("현재까지 획득한 총 포인트"),
                                fieldWithPath("totalPossibleReward").type(JsonFieldType.NUMBER)
                                        .description("모든 미션 완료시 획득 가능한 총 포인트"),
                                fieldWithPath("missions[]").type(JsonFieldType.ARRAY)
                                        .description("미션 리스트"),
                                fieldWithPath("missions[].missionHistoryId").type(JsonFieldType.NUMBER)
                                        .optional()
                                        .description("미션 이력 ID"),
                                fieldWithPath("missions[].missionId").type(JsonFieldType.NUMBER)
                                        .description("미션 ID"),
                                fieldWithPath("missions[].missionTitle").type(JsonFieldType.STRING)
                                        .description("미션 제목"),
                                fieldWithPath("missions[].missionContent").type(JsonFieldType.STRING)
                                        .description("미션 내용"),
                                fieldWithPath("missions[].category").type(JsonFieldType.STRING)
                                        .description("미션 카테고리: " + Arrays.toString(MissionCategoryStatus.values())),
                                fieldWithPath("missions[].reward").type(JsonFieldType.NUMBER)
                                        .description("미션 완료시 획득 포인트"),
                                fieldWithPath("missions[].status").type(JsonFieldType.STRING)
                                        .description("미션 상태: " + Arrays.toString(MissionStatus.values()))
                        )
                ));
    }
}
