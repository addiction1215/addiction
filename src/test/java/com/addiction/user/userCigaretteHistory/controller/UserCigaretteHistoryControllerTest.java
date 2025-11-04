package com.addiction.user.userCigaretteHistory.controller;

import com.addiction.ControllerTestSupport;
import com.addiction.user.userCigaretteHistory.enums.ComparisonType;
import com.addiction.user.userCigaretteHistory.enums.PeriodType;
import com.addiction.user.userCigaretteHistory.service.response.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserCigaretteHistoryControllerTest extends ControllerTestSupport {

    @DisplayName("월별 캘린더 흡연기록을 조회한다.")
    @Test
    @WithMockUser(roles = "USER")
    void 월별_캘린더_흡연기록을_조회한다() throws Exception {
        // given
        String month = "2024-07";
        List<UserCigaretteHistoryCalenderResponse> response = List.of(
                UserCigaretteHistoryCalenderResponse.builder()
                        .date("2024-07-01")
                        .count(5)
                        .build()
        );
        given(userCigaretteHistoryService.findCalendarByDate(month)).willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/user/cigarette-history/calendar")
                                .param("month", month)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].date").value("2024-07-01"))
                .andExpect(jsonPath("$.data[0].count").value(5));
    }

    @DisplayName("특정 날짜의 흡연기록을 조회한다.")
    @Test
    @WithMockUser(roles = "USER")
    void 특정_날짜의_흡연기록을_조회한다() throws Exception {
        // given
        String date = "2024-07-01";
        List<UserCigaretteHistoryResponse> response = List.of(
                UserCigaretteHistoryResponse.builder()
                        .address("서울시 강남구")
                        .smokePatienceTime(1800)
                        .build()
        );
        given(userCigaretteHistoryService.findHistoryByDate(date)).willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/user/cigarette-history/history")
                                .param("date", date)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].address").value("서울시 강남구"))
                .andExpect(jsonPath("$.data[0].smokePatienceTime").value(1800));
    }

    @DisplayName("기간별 흡연기록 그래프를 조회한다.")
    @Test
    @WithMockUser(roles = "USER")
    void 기간별_흡연기록_그래프를_조회한다() throws Exception {
        // given
        PeriodType periodType = PeriodType.WEEKLY;
        UserCigaretteHistoryGraphResponse response = UserCigaretteHistoryGraphResponse.builder().build();
        given(userCigaretteHistoryService.findGraphByPeriod(periodType)).willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/user/cigarette-history/graph")
                                .param("periodType", "WEEKLY")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("유저의 최신 흡연기록을 조회한다.")
    @Test
    @WithMockUser(roles = "USER")
    void 유저의_최신_흡연기록을_조회한다() throws Exception {
        // given
        UserCigaretteHistoryLastestResponse response = UserCigaretteHistoryLastestResponse.builder()
                .address("서울시 송파구")
                .lastDate(LocalDateTime.of(2024, 7, 21, 10, 0, 0))
                .build();
        given(userCigaretteHistoryService.findLastestByUserId()).willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/user/cigarette-history/latest")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.address").value("서울시 송파구"))
                .andExpect(jsonPath("$.data.lastDate").value("2024-07-21T10:00:00"));
    }


    @DisplayName("주간 흡연 횟수 비교 데이터를 조회한다.")
    @Test
    @WithMockUser(roles = "USER")
    void 주간_흡연_횟수_비교_데이터를_조회한다() throws Exception {
        // given
        ComparisonType comparisonType = ComparisonType.COUNT;
        WeeklyComparisonResponse response = WeeklyComparisonResponse.builder()
                .comparisonType(comparisonType)
                .lastWeekCount(10)
                .thisWeekCount(5)
                .difference(-5.0)
                .changeRate(-50.0)
                .build();

        given(userCigaretteHistoryService.compareWeekly(comparisonType)).willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/user/cigarette-history/weekly-comparison")
                                .param("comparisonType", "COUNT")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.comparisonType").value("COUNT"))
                .andExpect(jsonPath("$.data.lastWeekCount").value(10))
                .andExpect(jsonPath("$.data.thisWeekCount").value(5))
                .andExpect(jsonPath("$.data.difference").value(-5.0))
                .andExpect(jsonPath("$.data.changeRate").value(-50.0));
    }

    @DisplayName("주간 평균 금연 시간 비교 데이터를 조회한다.")
    @Test
    @WithMockUser(roles = "USER")
    void 주간_평균_금연_시간_비교_데이터를_조회한다() throws Exception {
        // given
        ComparisonType comparisonType = ComparisonType.TIME;
        WeeklyComparisonResponse response = WeeklyComparisonResponse.builder()
                .comparisonType(comparisonType)
                .lastWeekAvgTime(60.5)
                .thisWeekAvgTime(70.5)
                .difference(10.0)
                .changeRate(16.5)
                .build();

        given(userCigaretteHistoryService.compareWeekly(comparisonType)).willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/user/cigarette-history/weekly-comparison")
                                .param("comparisonType", "TIME")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.comparisonType").value("TIME"))
                .andExpect(jsonPath("$.data.lastWeekAvgTime").value(60.5))
                .andExpect(jsonPath("$.data.thisWeekAvgTime").value(70.5))
                .andExpect(jsonPath("$.data.difference").value(10.0))
                .andExpect(jsonPath("$.data.changeRate").value(16.5));
    }

    @DisplayName("이번 주 흡연량을 조회한다.")
    @Test
    @WithMockUser(roles = "USER")
    void 이번_주_흡연량을_조회한다() throws Exception {
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

        given(userCigaretteHistoryService.findThisWeekCigarettes()).willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/user/cigarette-history/this-week")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.weekData").isArray())
                .andExpect(jsonPath("$.data.weekData.length()").value(7))
                .andExpect(jsonPath("$.data.weekData[0].date").value("20251026"))
                .andExpect(jsonPath("$.data.weekData[0].dayOfWeek").value("SUN"))
                .andExpect(jsonPath("$.data.weekData[0].count").value(5))
                .andExpect(jsonPath("$.data.weekData[1].date").value("20251027"))
                .andExpect(jsonPath("$.data.weekData[1].dayOfWeek").value("MON"))
                .andExpect(jsonPath("$.data.weekData[1].count").value(3))
                .andExpect(jsonPath("$.data.weekData[2].date").value("20251028"))
                .andExpect(jsonPath("$.data.weekData[2].dayOfWeek").value("TUE"))
                .andExpect(jsonPath("$.data.weekData[2].count").value(7))
                .andExpect(jsonPath("$.data.weekData[3].date").value("20251029"))
                .andExpect(jsonPath("$.data.weekData[3].dayOfWeek").value("WED"))
                .andExpect(jsonPath("$.data.weekData[3].count").value(2))
                .andExpect(jsonPath("$.data.weekData[4].date").value("20251030"))
                .andExpect(jsonPath("$.data.weekData[4].dayOfWeek").value("THU"))
                .andExpect(jsonPath("$.data.weekData[4].count").value(0))
                .andExpect(jsonPath("$.data.weekData[5].date").value("20251031"))
                .andExpect(jsonPath("$.data.weekData[5].dayOfWeek").value("FRI"))
                .andExpect(jsonPath("$.data.weekData[5].count").value(0))
                .andExpect(jsonPath("$.data.weekData[6].date").value("20251101"))
                .andExpect(jsonPath("$.data.weekData[6].dayOfWeek").value("SAT"))
                .andExpect(jsonPath("$.data.weekData[6].count").value(0));
    }

    @DisplayName("금연 피드백을 조회한다.")
    @Test
    @WithMockUser(roles = "USER")
    void 금연_피드백을_조회한다() throws Exception {
        // given
        SmokingFeedbackResponse response = SmokingFeedbackResponse.builder()
                .message("큰 성과에요! 지금 흐름을 이어가면 금연이 훨씬 가까워져요.")
                .build();

        given(userCigaretteHistoryService.getSmokingFeedback()).willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/user/cigarette-history/feedback")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("큰 성과에요! 지금 흐름을 이어가면 금연이 훨씬 가까워져요."));
    }

    @DisplayName("완전 금연 상태의 피드백을 조회한다.")
    @Test
    @WithMockUser(roles = "USER")
    void 완전_금연_상태의_피드백을_조회한다() throws Exception {
        // given
        SmokingFeedbackResponse response = SmokingFeedbackResponse.builder()
                .message("어제 단 한 개비도 안 피셨군요! 금연 완성 단계입니다. 작은 보상 습관으로 유지하세요.")
                .build();

        given(userCigaretteHistoryService.getSmokingFeedback()).willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/user/cigarette-history/feedback")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("어제 단 한 개비도 안 피셨군요! 금연 완성 단계입니다. 작은 보상 습관으로 유지하세요."));
    }

    @DisplayName("흡연량이 증가한 상태의 피드백을 조회한다.")
    @Test
    @WithMockUser(roles = "USER")
    void 흡연량이_증가한_상태의_피드백을_조회한다() throws Exception {
        // given
        SmokingFeedbackResponse response = SmokingFeedbackResponse.builder()
                .message("흡연량이 폭발적으로 늘었어요. 지금은 전문가 도움도 고려해 보세요.")
                .build();

        given(userCigaretteHistoryService.getSmokingFeedback()).willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/user/cigarette-history/feedback")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("흡연량이 폭발적으로 늘었어요. 지금은 전문가 도움도 고려해 보세요."));
    }
}