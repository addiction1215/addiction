package docs.challenge.mission;

import com.addiction.challenge.mission.controller.MissionController;
import com.addiction.challenge.mission.entity.MissionCategoryStatus;
import com.addiction.challenge.mission.service.MissionReadService;
import com.addiction.challenge.mission.service.response.MissionListResponse;
import com.addiction.challenge.mission.service.response.MissionResponse;
import com.addiction.challenge.missionhistory.entity.MissionStatus;
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

public class MissionControllerDocsTest extends RestDocsSupport {

    private final MissionReadService missionReadService = mock(MissionReadService.class);

    @Override
    protected Object initController() {
        return new MissionController(missionReadService);
    }

    @DisplayName("챌린지 미션 리스트 조회 API")
    @Test
    void 챌린지_미션_리스트_조회_API() throws Exception {
        // given
        List<MissionResponse> missionList = List.of(
                MissionResponse.builder()
                        .missionId(1L)
                        .title("평소 흡연 루트 피해보기")
                        .content("평소 다니는 흡연 루트를 피해서 이동하기")
                        .reward(50)
                        .category(MissionCategoryStatus.LOCATION)
                        .status(MissionStatus.COMPLETED)
                        .build(),
                MissionResponse.builder()
                        .missionId(2L)
                        .title("평소 흡연 시간대 산책하기")
                        .content("평소 흡연하는 시간대에 산책해보기")
                        .reward(70)
                        .category(MissionCategoryStatus.REPLACE_ACTION)
                        .status(MissionStatus.PROGRESSING)
                        .build(),
                MissionResponse.builder()
                        .missionId(3L)
                        .title("5분간 참기")
                        .content("흡연 욕구가 생길 때 5분간 참아보기")
                        .reward(30)
                        .category(MissionCategoryStatus.HOLD)
                        .status(MissionStatus.PROGRESSING)
                        .build()
        );

        MissionListResponse response = MissionListResponse.builder()
                .challengeId(1L)
                .challengeTitle("유혹 피하기")
                .totalMissionCount(7)
                .totalReward(500)
                .missions(missionList)
                .build();

        given(missionReadService.getMissionListByChallengeId(1L))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/mission/challenge/{challengeId}", 1L)
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("mission-get-list-by-challenge",
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
                                fieldWithPath("data.challengeId").type(JsonFieldType.NUMBER)
                                        .description("챌린지 ID"),
                                fieldWithPath("data.challengeTitle").type(JsonFieldType.STRING)
                                        .description("챌린지 제목"),
                                fieldWithPath("data.totalMissionCount").type(JsonFieldType.NUMBER)
                                        .description("전체 미션 개수"),
                                fieldWithPath("data.totalReward").type(JsonFieldType.NUMBER)
                                        .description("완료시 최종 획득 포인트"),
                                fieldWithPath("data.missions[]").type(JsonFieldType.ARRAY)
                                        .description("미션 리스트"),
                                fieldWithPath("data.missions[].missionId").type(JsonFieldType.NUMBER)
                                        .description("미션 ID"),
                                fieldWithPath("data.missions[].title").type(JsonFieldType.STRING)
                                        .description("미션 제목"),
                                fieldWithPath("data.missions[].content").type(JsonFieldType.STRING)
                                        .description("미션 내용"),
                                fieldWithPath("data.missions[].reward").type(JsonFieldType.NUMBER)
                                        .description("미션 완료시 획득 포인트"),
                                fieldWithPath("data.missions[].category").type(JsonFieldType.STRING)
                                        .description("미션 카테고리: " + Arrays.toString(MissionCategoryStatus.values())),
                                fieldWithPath("data.missions[].status").type(JsonFieldType.STRING)
                                        .description("미션 상태: " + Arrays.toString(MissionStatus.values()))
                        )
                ));
    }
}
