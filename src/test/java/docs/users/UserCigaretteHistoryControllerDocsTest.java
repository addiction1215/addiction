package docs.users;

import com.addiction.user.userCigaretteHistory.controller.UserCigaretteHistoryController;
import com.addiction.user.userCigaretteHistory.enums.ComparisonType;
import com.addiction.user.userCigaretteHistory.enums.PeriodType;
import com.addiction.user.userCigaretteHistory.service.UserCigaretteHistoryService;
import com.addiction.user.userCigaretteHistory.service.response.*;
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
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserCigaretteHistoryControllerDocsTest extends RestDocsSupport {
    private final UserCigaretteHistoryService userCigaretteHistoryService = mock(UserCigaretteHistoryService.class);

    @Override
    protected Object initController() {
        return new UserCigaretteHistoryController(userCigaretteHistoryService);
    }

    @DisplayName("월별 캘린더 흡연기록 조회 API")
    @Test
    void 월별_캘린더_흡연기록_조회_API() throws Exception {
        List<UserCigaretteHistoryCalenderResponse> response = List.of(
                UserCigaretteHistoryCalenderResponse.builder()
                        .date("2024-06-01")
                        .count(5)
                        .build()
        );
        given(userCigaretteHistoryService.findCalendarByDate(any()))
                .willReturn(response);

        mockMvc.perform(
                        get("/api/v1/user/cigarette-history/calendar")
                                .param("month", "2024-06")
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-cigarette-history-calendar",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("month").description("조회할 월 (yyyy-MM)")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("httpStatus").type(JsonFieldType.STRING).description("HTTP 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("월별 캘린더 흡연기록 목록"),
                                fieldWithPath("data[].date").type(JsonFieldType.STRING).description("날짜"),
                                fieldWithPath("data[].count").type(JsonFieldType.NUMBER).description("흡연 개수")
                        )
                ));
    }

    @DisplayName("특정 날짜 흡연기록 조회 API")
    @Test
    void 특정_날짜_흡연기록_조회_API() throws Exception {
        List<UserCigaretteHistoryResponse> response = List.of(
                UserCigaretteHistoryResponse.builder()
                        .address("서울시 강남구 역삼동")
                        .smokePatienceTime(900)
                        .build()
        );
        given(userCigaretteHistoryService.findHistoryByDate(any()))
                .willReturn(response);

        mockMvc.perform(
                        get("/api/v1/user/cigarette-history/history")
                                .param("date", "2024-06-01")
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-cigarette-history-history",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("date").description("조회할 날짜 (yyyy-MM-dd)")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("httpStatus").type(JsonFieldType.STRING).description("HTTP 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("흡연기록 목록"),
                                fieldWithPath("data[].address").type(JsonFieldType.STRING).description("흡연 장소"),
                                fieldWithPath("data[].smokePatienceTime").type(JsonFieldType.NUMBER).description("인내 시간(초)")
                        )
                ));
    }

    @DisplayName("기간별 흡연기록 그래프 조회 API")
    @Test
    void 기간별_흡연기록_그래프_조회_API() throws Exception {
        UserCigaretteHistoryGraphResponse response = UserCigaretteHistoryGraphResponse.builder()
                .cigarette(UserCigaretteHistoryGraphCountResponse.builder()
                        .avgCigaretteCount(10)
                        .date(List.of(
                                UserCigaretteHistoryGraphDateResponse.builder()
                                        .date("2024-06-01")
                                        .value(5)
                                        .build(),
                                UserCigaretteHistoryGraphDateResponse.builder()
                                        .date("2024-06-02")
                                        .value(5)
                                        .build()
                        )).build())
                .patient(UserCigaretteHistoryGraphPatientResponse.builder()
                        .avgSmokePatientTime(100)
                        .date(List.of(
                                UserCigaretteHistoryGraphDateResponse.builder()
                                        .date("2024-06-01")
                                        .value(5)
                                        .build(),
                                UserCigaretteHistoryGraphDateResponse.builder()
                                        .date("2024-06-02")
                                        .value(5)
                                        .build()
                        )).build())
                .build();

        given(userCigaretteHistoryService.findGraphByPeriod(any()))
                .willReturn(response);

        mockMvc.perform(
                        get("/api/v1/user/cigarette-history/graph")
                                .param("periodType", "WEEKLY")
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-cigarette-history-graph",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("periodType").description(
                                        "기간 타입 ->가능한 값: " + Arrays.toString(PeriodType.values()))
                        ),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("httpStatus").type(JsonFieldType.STRING).description("HTTP 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("그래프 데이터"),
                                fieldWithPath("data.cigarette").type(JsonFieldType.OBJECT).description("담배 관련 그래프 데이터"),
                                fieldWithPath("data.cigarette.avgCigaretteCount").type(JsonFieldType.NUMBER)
                                        .description("평균 흡연 개수"),
                                fieldWithPath("data.cigarette.date").type(JsonFieldType.ARRAY).description("날짜별 흡연 데이터"),
                                fieldWithPath("data.cigarette.date[].date").type(JsonFieldType.STRING).description("날짜"),
                                fieldWithPath("data.cigarette.date[].value").type(JsonFieldType.NUMBER).description("해당 날짜의 흡연 개수"),
                                fieldWithPath("data.patient").type(JsonFieldType.OBJECT).description("인내 관련 그래프 데이터"),
                                fieldWithPath("data.patient.avgSmokePatientTime").type(JsonFieldType.NUMBER)
                                        .description("평균 인내 시간(초)"),
                                fieldWithPath("data.patient.date").type(JsonFieldType.ARRAY).description("날짜별 인내 데이터"),
                                fieldWithPath("data.patient.date[].date").type(JsonFieldType.STRING).description("날짜"),
                                fieldWithPath("data.patient.date[].value").type(JsonFieldType.NUMBER).description("해당 날짜의 인내 시간(초)")
                        )
                ));
    }

    @DisplayName("userId로 최신 흡연기록 조회 API")
    @Test
    void userId로_최신_흡연기록_조회_API() throws Exception {
        // given
        given(userCigaretteHistoryService.findLastestByUserId())
                .willReturn(UserCigaretteHistoryLastestResponse.builder()
                        .address("서울시 송파구")
                        .lastDate(LocalDateTime.parse("2024-06-10T10:00:00"))
                        .build());

        mockMvc.perform(
                        get("/api/v1/user/cigarette-history/latest")
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-cigarette-history-latest",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("httpStatus").type(JsonFieldType.STRING).description("HTTP 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("최신 흡연 기록"),
                                fieldWithPath("data.address").type(JsonFieldType.STRING).description("마지막 흡연 장소"),
                                fieldWithPath("data.lastDate").type(JsonFieldType.STRING).description("마지막 흡연일시")
                        )
                ));
    }

    @DisplayName("주간 흡연 데이터 비교 API")
    @Test
    void 주간_흡연_데이터_비교_API() throws Exception {
        // given
        WeeklyComparisonResponse countResponse = WeeklyComparisonResponse.builder()
                .comparisonType(ComparisonType.COUNT)
                .lastWeekCount(10)
                .thisWeekCount(5)
                .difference(-5.0)
                .changeRate(-50.0)
                .build();

        given(userCigaretteHistoryService.compareWeekly(ComparisonType.COUNT))
                .willReturn(countResponse);

        WeeklyComparisonResponse timeResponse = WeeklyComparisonResponse.builder()
                .comparisonType(ComparisonType.TIME)
                .lastWeekAvgTime(60.5)
                .thisWeekAvgTime(70.5)
                .difference(10.0)
                .changeRate(16.5)
                .build();

        given(userCigaretteHistoryService.compareWeekly(ComparisonType.TIME))
                .willReturn(timeResponse);

        // when // then
        mockMvc.perform(
                        get("/api/v1/user/cigarette-history/weekly-comparison")
                                .param("comparisonType", "COUNT")
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-cigarette-history-weekly-comparison",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("comparisonType").description("비교 타입 (COUNT: 흡연 횟수, TIME: 평균 금연 시간)")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("httpStatus").type(JsonFieldType.STRING).description("HTTP 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("주간 비교 데이터"),
                                fieldWithPath("data.comparisonType").type(JsonFieldType.STRING).description("비교 타입"),
                                fieldWithPath("data.lastWeekCount").type(JsonFieldType.NUMBER).description("지난주 총 횟수"),
                                fieldWithPath("data.thisWeekCount").type(JsonFieldType.NUMBER).description("이번주 총 횟수"),
                                fieldWithPath("data.lastWeekAvgTime").type(JsonFieldType.NULL).description("지난주 평균 금연 시간 (TIME 타입)"),
                                fieldWithPath("data.thisWeekAvgTime").type(JsonFieldType.NULL).description("이번주 평균 금연 시간 (TIME 타입)"),
                                fieldWithPath("data.difference").type(JsonFieldType.NUMBER).description("증감 값"),
                                fieldWithPath("data.changeRate").type(JsonFieldType.NUMBER).description("증감률 (%)")
                        )
                ));
    }

    @DisplayName("이번 주 흡연량 조회 API")
    @Test
    void 이번_주_흡연량_조회_API() throws Exception {
        // given
        WeeklyCigaretteResponse response = WeeklyCigaretteResponse.builder()
                .weekData(List.of(
                        WeeklyCigaretteResponse.DailyCigaretteCount.builder()
                                .date("20251026")
                                .dayOfWeek("SUN")
                                .count(5)
                                .build(),
                        WeeklyCigaretteResponse.DailyCigaretteCount.builder()
                                .date("20251027")
                                .dayOfWeek("MON")
                                .count(3)
                                .build(),
                        WeeklyCigaretteResponse.DailyCigaretteCount.builder()
                                .date("20251028")
                                .dayOfWeek("TUE")
                                .count(7)
                                .build(),
                        WeeklyCigaretteResponse.DailyCigaretteCount.builder()
                                .date("20251029")
                                .dayOfWeek("WED")
                                .count(2)
                                .build(),
                        WeeklyCigaretteResponse.DailyCigaretteCount.builder()
                                .date("20251030")
                                .dayOfWeek("THU")
                                .count(0)
                                .build(),
                        WeeklyCigaretteResponse.DailyCigaretteCount.builder()
                                .date("20251031")
                                .dayOfWeek("FRI")
                                .count(0)
                                .build(),
                        WeeklyCigaretteResponse.DailyCigaretteCount.builder()
                                .date("20251101")
                                .dayOfWeek("SAT")
                                .count(0)
                                .build()
                ))
                .build();

        given(userCigaretteHistoryService.findThisWeekCigarettes())
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/user/cigarette-history/this-week")
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-cigarette-history-this-week",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("httpStatus").type(JsonFieldType.STRING).description("HTTP 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("이번 주 흡연량 데이터"),
                                fieldWithPath("data.weekData").type(JsonFieldType.ARRAY).description("일요일부터 토요일까지의 흡연 데이터"),
                                fieldWithPath("data.weekData[].date").type(JsonFieldType.STRING).description("날짜 (yyyyMMdd 형식)"),
                                fieldWithPath("data.weekData[].dayOfWeek").type(JsonFieldType.STRING).description("요일 (SUN, MON, TUE, WED, THU, FRI, SAT)"),
                                fieldWithPath("data.weekData[].count").type(JsonFieldType.NUMBER).description("해당 날짜의 흡연 개피 수")
                        )
                ));
    }
}