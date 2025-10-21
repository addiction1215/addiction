package com.addiction.user.userCigaretteHistory.controller;

import java.util.List;

import com.addiction.user.userCigaretteHistory.enums.ComparisonType;
import com.addiction.user.userCigaretteHistory.service.response.UserCigaretteHistoryLastestResponse;
import com.addiction.user.userCigaretteHistory.service.response.WeeklyComparisonResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.addiction.global.ApiResponse;
import com.addiction.user.userCigaretteHistory.enums.PeriodType;
import com.addiction.user.userCigaretteHistory.service.response.UserCigaretteHistoryCalenderResponse;
import com.addiction.user.userCigaretteHistory.service.UserCigaretteHistoryService;
import com.addiction.user.userCigaretteHistory.service.response.UserCigaretteHistoryGraphResponse;
import com.addiction.user.userCigaretteHistory.service.response.UserCigaretteHistoryResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/cigarette-history")
public class UserCigaretteHistoryController {

	private final UserCigaretteHistoryService userCigaretteHistoryService;

	@GetMapping("/calendar")
	public ApiResponse<List<UserCigaretteHistoryCalenderResponse>> findCalendarByDate(String month) {
		return ApiResponse.ok(userCigaretteHistoryService.findCalendarByDate(month));
	}

	@GetMapping("/history")
	public ApiResponse<List<UserCigaretteHistoryResponse>> findHistoryByDate(String date) {
		return ApiResponse.ok(userCigaretteHistoryService.findHistoryByDate(date));
	}

	@GetMapping("/graph")
	public ApiResponse<UserCigaretteHistoryGraphResponse> findGraphByPeriod(PeriodType periodType) {
		return ApiResponse.ok(userCigaretteHistoryService.findGraphByPeriod(periodType));
	}

    @GetMapping("/latest")
    public ApiResponse<UserCigaretteHistoryLastestResponse> findLastestByUserId() {
        return ApiResponse.ok(userCigaretteHistoryService.findLastestByUserId());
    }

	/**
	 * 주간 흡연 데이터 비교 API
	 * 지난주(월~일) vs 이번주(월~오늘) 비교
	 * 
	 * @param comparisonType COUNT(흡연 횟수) 또는 TIME(평균 금연 시간)
	 * @return 주간 비교 데이터
	 */
	@GetMapping("/weekly-comparison")
	public ApiResponse<WeeklyComparisonResponse> getWeeklyComparison(
			@RequestParam ComparisonType comparisonType) {
		return ApiResponse.ok(userCigaretteHistoryService.compareWeekly(comparisonType));
	}

}
