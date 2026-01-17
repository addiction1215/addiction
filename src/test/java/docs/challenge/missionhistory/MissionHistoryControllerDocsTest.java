package docs.challenge.missionhistory;

import com.addiction.challenge.mission.entity.MissionCategoryStatus;
import com.addiction.challenge.missionhistory.controller.MissionHistoryController;
import com.addiction.challenge.missionhistory.controller.request.MissionSubmitRequest;
import com.addiction.challenge.missionhistory.entity.MissionStatus;
import com.addiction.challenge.missionhistory.service.MissionHistoryReadService;
import com.addiction.challenge.missionhistory.service.MissionHistoryService;
import com.addiction.challenge.missionhistory.service.response.MissionDetailResponse;
import com.addiction.challenge.missionhistory.service.response.MissionHistoryResponse;
import com.addiction.challenge.missionhistory.service.response.MissionProgressResponse;
import com.addiction.challenge.missionhistory.service.response.MissionSubmitResponse;
import docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MissionHistoryControllerDocsTest extends RestDocsSupport {

    private final MissionHistoryReadService missionHistoryReadService = mock(MissionHistoryReadService.class);
    private final MissionHistoryService missionHistoryService = mock(MissionHistoryService.class);

    @Override
    protected Object initController() {
        return new MissionHistoryController(missionHistoryReadService, missionHistoryService);
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
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
                                        .description("응답 코드"),
                                fieldWithPath("httpStatus").type(JsonFieldType.STRING)
                                        .description("HTTP 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.challengeHistoryId").type(JsonFieldType.NUMBER)
                                        .description("챌린지 이력 ID"),
                                fieldWithPath("data.challengeId").type(JsonFieldType.NUMBER)
                                        .description("챌린지 ID"),
                                fieldWithPath("data.challengeTitle").type(JsonFieldType.STRING)
                                        .description("챌린지 제목"),
                                fieldWithPath("data.totalMissionCount").type(JsonFieldType.NUMBER)
                                        .description("전체 미션 개수"),
                                fieldWithPath("data.completedMissionCount").type(JsonFieldType.NUMBER)
                                        .description("완료한 미션 개수"),
                                fieldWithPath("data.totalEarnedReward").type(JsonFieldType.NUMBER)
                                        .description("현재까지 획득한 총 포인트"),
                                fieldWithPath("data.totalPossibleReward").type(JsonFieldType.NUMBER)
                                        .description("모든 미션 완료시 획득 가능한 총 포인트"),
                                fieldWithPath("data.missions[]").type(JsonFieldType.ARRAY)
                                        .description("미션 리스트"),
                                fieldWithPath("data.missions[].missionHistoryId").type(JsonFieldType.NUMBER)
                                        .description("미션 이력 ID"),
                                fieldWithPath("data.missions[].missionId").type(JsonFieldType.NUMBER)
                                        .description("미션 ID"),
                                fieldWithPath("data.missions[].missionTitle").type(JsonFieldType.STRING)
                                        .description("미션 제목"),
                                fieldWithPath("data.missions[].missionContent").type(JsonFieldType.STRING)
                                        .description("미션 내용"),
                                fieldWithPath("data.missions[].category").type(JsonFieldType.STRING)
                                        .description("미션 카테고리: " + Arrays.toString(MissionCategoryStatus.values())),
                                fieldWithPath("data.missions[].reward").type(JsonFieldType.NUMBER)
                                        .description("미션 완료시 획득 포인트"),
                                fieldWithPath("data.missions[].status").type(JsonFieldType.STRING)
                                        .description("미션 상태: " + Arrays.toString(MissionStatus.values()))
                        )
                ));
    }

    @DisplayName("미션 상세 조회 API")
    @Test
    void 미션_상세_조회_API() throws Exception {
        // given
        MissionDetailResponse response = MissionDetailResponse.builder()
                .missionId(1L)
                .title("평소 흡연 루트 피해보기")
                .content("평소 다니는 흡연 루트를 피해서 이동하기")
                .reward(50)
                .category(MissionCategoryStatus.LOCATION)
                .missionHistoryId(101L)
                .status(MissionStatus.PROGRESSING)
                .completeAt(null)
                .address("서울시 강남구")
                .gpsVerifyCount(2)
                .photoUrl1(null)
                .photoUrl2(null)
                .photoUrl3(null)
                .abstinenceTime(null)
                .build();

        given(missionHistoryService.getMissionDetail(anyLong()))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/mission-history/detail/{missionHistoryId}", 101L)
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("mission-history-get-detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("missionHistoryId").description("미션 이력 ID")
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
                                fieldWithPath("data.missionId").type(JsonFieldType.NUMBER)
                                        .description("미션 ID"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING)
                                        .description("미션 제목"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING)
                                        .description("미션 내용"),
                                fieldWithPath("data.reward").type(JsonFieldType.NUMBER)
                                        .description("미션 완료시 획득 포인트"),
                                fieldWithPath("data.category").type(JsonFieldType.STRING)
                                        .description("미션 카테고리: " + Arrays.toString(MissionCategoryStatus.values())),
                                fieldWithPath("data.missionHistoryId").type(JsonFieldType.NUMBER)
                                        .description("미션 이력 ID"),
                                fieldWithPath("data.status").type(JsonFieldType.STRING)
                                        .description("미션 상태: " + Arrays.toString(MissionStatus.values())),
                                fieldWithPath("data.completeAt").type(JsonFieldType.NULL)
                                        .description("미션 완료 시간").optional(),
                                fieldWithPath("data.address").type(JsonFieldType.STRING)
                                        .description("GPS 주소").optional(),
                                fieldWithPath("data.gpsVerifyCount").type(JsonFieldType.NUMBER)
                                        .description("GPS 검증 횟수").optional(),
                                fieldWithPath("data.photoUrl1").type(JsonFieldType.NULL)
                                        .description("사진 URL 1").optional(),
                                fieldWithPath("data.photoUrl2").type(JsonFieldType.NULL)
                                        .description("사진 URL 2").optional(),
                                fieldWithPath("data.photoUrl3").type(JsonFieldType.NULL)
                                        .description("사진 URL 3").optional(),
                                fieldWithPath("data.abstinenceTime").type(JsonFieldType.NULL)
                                        .description("금연 시간 (초)").optional()
                        )
                ));
    }

    @DisplayName("미션 중간 제출 API - GPS")
    @Test
    void 미션_중간_제출_API_GPS() throws Exception {
        // given
        MissionSubmitRequest request = MissionSubmitRequest.builder()
                .missionHistoryId(101L)
                .address("서울시 강남구 테헤란로")
                .build();

        MissionSubmitResponse response = MissionSubmitResponse.builder()
                .missionHistoryId(101L)
                .build();

        given(missionHistoryService.submitMission(any()))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        post("/api/v1/mission-history/submit")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("mission-history-submit-gps",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("missionHistoryId").type(JsonFieldType.NUMBER)
                                        .description("미션 이력 ID"),
                                fieldWithPath("address").type(JsonFieldType.STRING)
                                        .description("GPS 주소 (GPS 미션일 경우 필수)").optional(),
                                fieldWithPath("photoUrl").type(JsonFieldType.NULL)
                                        .description("사진 URL (사진 미션일 경우 필수)").optional(),
                                fieldWithPath("photoNumber").type(JsonFieldType.NULL)
                                        .description("사진 번호 1~3 (사진 미션일 경우 필수)").optional(),
                                fieldWithPath("time").type(JsonFieldType.NULL)
                                        .description("금연 시간 초 단위 (흡연 참기 미션일 경우 필수)").optional()
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
                                fieldWithPath("data.missionHistoryId").type(JsonFieldType.NUMBER)
                                        .description("미션 이력 ID")
                        )
                ));
    }

    @DisplayName("미션 중간 제출 API - 사진")
    @Test
    void 미션_중간_제출_API_사진() throws Exception {
        // given
        MissionSubmitRequest request = MissionSubmitRequest.builder()
                .missionHistoryId(102L)
                .photoUrl("https://example.com/photo1.jpg")
                .build();

        MissionSubmitResponse response = MissionSubmitResponse.builder()
                .missionHistoryId(102L)
                .build();

        given(missionHistoryService.submitMission(any()))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        post("/api/v1/mission-history/submit")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("mission-history-submit-photo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("missionHistoryId").type(JsonFieldType.NUMBER)
                                        .description("미션 이력 ID"),
                                fieldWithPath("address").type(JsonFieldType.NULL)
                                        .description("GPS 주소 (GPS 미션일 경우 필수)").optional(),
                                fieldWithPath("photoUrl").type(JsonFieldType.STRING)
                                        .description("사진 URL (사진 미션일 경우 필수)").optional(),
                                fieldWithPath("photoNumber").type(JsonFieldType.NUMBER)
                                        .description("사진 번호 1~3 (사진 미션일 경우 필수)").optional(),
                                fieldWithPath("time").type(JsonFieldType.NULL)
                                        .description("금연 시간 초 단위 (흡연 참기 미션일 경우 필수)").optional()
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
                                fieldWithPath("data.missionHistoryId").type(JsonFieldType.NUMBER)
                                        .description("미션 이력 ID")
                        )
                ));
    }

    @DisplayName("미션 중간 제출 API")
    @Test
    void 미션_중간_제출_API_흡연참기() throws Exception {
        // given
        MissionSubmitRequest request = MissionSubmitRequest.builder()
                .missionHistoryId(103L)
                .time(3600)
                .build();

        MissionSubmitResponse response = MissionSubmitResponse.builder()
                .missionHistoryId(103L)
                .build();

        given(missionHistoryService.submitMission(any()))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        post("/api/v1/mission-history/submit")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("mission-history-submit-abstinence",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("missionHistoryId").type(JsonFieldType.NUMBER)
                                        .description("미션 이력 ID"),
                                fieldWithPath("address").type(JsonFieldType.NULL)
                                        .description("GPS 주소 (GPS 미션일 경우 필수)").optional(),
                                fieldWithPath("photoUrl").type(JsonFieldType.NULL)
                                        .description("사진 URL (사진 미션일 경우 필수)").optional(),
                                fieldWithPath("time").type(JsonFieldType.NUMBER)
                                        .description("금연 시간 초 단위 (흡연 참기 미션일 경우 필수)").optional()
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
                                fieldWithPath("data.missionHistoryId").type(JsonFieldType.NUMBER)
                                        .description("미션 이력 ID")
                        )
                ));
    }

    @DisplayName("미션 최종 제출 API")
    @Test
    void 미션_최종_제출_API() throws Exception {
        // given
        MissionSubmitResponse response = MissionSubmitResponse.builder()
                .missionHistoryId(101L)
                .build();

        given(missionHistoryService.completeMission(anyLong()))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        patch("/api/v1/mission-history/complete/{missionHistoryId}", 101L)
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("mission-history-complete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("missionHistoryId").description("미션 이력 ID")
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
                                fieldWithPath("data.missionHistoryId").type(JsonFieldType.NUMBER)
                                        .description("미션 이력 ID")
                        )
                ));
    }
}
