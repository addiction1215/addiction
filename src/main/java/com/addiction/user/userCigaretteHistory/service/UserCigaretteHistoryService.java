package com.addiction.user.userCigaretteHistory.service;

import java.util.List;

import com.addiction.user.userCigaretteHistory.document.CigaretteHistoryDocument;
import com.addiction.user.userCigaretteHistory.enums.ComparisonType;
import com.addiction.user.userCigaretteHistory.enums.PeriodType;
import com.addiction.user.userCigaretteHistory.service.response.UserCigaretteHistoryCalenderResponse;
import com.addiction.user.userCigaretteHistory.service.response.UserCigaretteHistoryGraphResponse;
import com.addiction.user.userCigaretteHistory.service.response.UserCigaretteHistoryLastestResponse;
import com.addiction.user.userCigaretteHistory.service.response.UserCigaretteHistoryResponse;
import com.addiction.user.userCigaretteHistory.service.response.WeeklyComparisonResponse;

public interface UserCigaretteHistoryService {

	void save(String monthStr, String dateStr, Long userId, Integer smokeCount, Long avgPatienceTime,
		List<CigaretteHistoryDocument.History> historyList);

	List<UserCigaretteHistoryCalenderResponse> findCalendarByDate(String date);

	List<UserCigaretteHistoryResponse> findHistoryByDate(String date);

	UserCigaretteHistoryGraphResponse findGraphByPeriod(PeriodType periodType);

    UserCigaretteHistoryLastestResponse findLastestByUserId();

	/**
	 * 지난주 vs 이번주 흡연 데이터 비교
	 * @param comparisonType COUNT(횟수) 또는 TIME(시간)
	 * @return 주간 비교 응답
	 */
	WeeklyComparisonResponse compareWeekly(ComparisonType comparisonType);

}
