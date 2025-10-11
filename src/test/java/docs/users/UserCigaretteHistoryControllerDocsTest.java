package docs.users;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.addiction.user.userCigaretteHistory.service.response.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import com.addiction.user.userCigaretteHistory.controller.UserCigaretteHistoryController;
import com.addiction.user.userCigaretteHistory.enums.PeriodType;
import com.addiction.user.userCigaretteHistory.service.UserCigaretteHistoryService;

import docs.RestDocsSupport;

public class UserCigaretteHistoryControllerDocsTest extends RestDocsSupport {
	private final UserCigaretteHistoryService userCigaretteHistoryService = mock(UserCigaretteHistoryService.class);

	@Override
	protected Object initController() {
		return new UserCigaretteHistoryController(userCigaretteHistoryService);
	}

	@DisplayName("월별 캘린더 흡연기록 조회 API")
	@Test
	void findCalendarByDate() throws Exception {
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
	void findHistoryByDate() throws Exception {
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
	void findGraphByPeriod() throws Exception {
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
    void findLastestByUserId() throws Exception {
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
}
