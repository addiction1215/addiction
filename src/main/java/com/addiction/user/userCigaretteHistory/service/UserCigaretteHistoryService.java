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
import com.addiction.user.userCigaretteHistory.service.response.WeeklyCigaretteResponse;
import com.addiction.user.userCigaretteHistory.service.response.SmokingFeedbackResponse;

public interface UserCigaretteHistoryService {

	void save(String monthStr, String dateStr, Long userId, Integer smokeCount, Long avgPatienceTime,
		List<CigaretteHistoryDocument.History> historyList);

	List<UserCigaretteHistoryCalenderResponse> findCalendarByDate(String date);

	List<UserCigaretteHistoryResponse> findHistoryByDate(String date);

	UserCigaretteHistoryGraphResponse findGraphByPeriod(PeriodType periodType);

    UserCigaretteHistoryLastestResponse findLastestByUserId();

	WeeklyComparisonResponse compareWeekly(ComparisonType comparisonType);

	WeeklyCigaretteResponse findThisWeekCigarettes();

	SmokingFeedbackResponse getSmokingFeedback();

}
