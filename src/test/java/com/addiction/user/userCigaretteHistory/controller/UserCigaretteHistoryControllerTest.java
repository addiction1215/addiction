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
}