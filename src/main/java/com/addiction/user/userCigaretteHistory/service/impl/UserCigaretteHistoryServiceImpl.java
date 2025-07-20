package com.addiction.user.userCigaretteHistory.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.addiction.global.security.SecurityService;
import com.addiction.user.userCigaretteHistory.document.CigaretteHistoryDocument;
import com.addiction.user.userCigaretteHistory.enums.PeriodType;
import com.addiction.user.userCigaretteHistory.repository.UserCigaretteHistoryRepository;
import com.addiction.user.userCigaretteHistory.service.UserCigaretteHistoryService;
import com.addiction.user.userCigaretteHistory.service.response.UserCigaretteHistoryCalenderResponse;
import com.addiction.user.userCigaretteHistory.service.response.UserCigaretteHistoryGraphCountResponse;
import com.addiction.user.userCigaretteHistory.service.response.UserCigaretteHistoryGraphDateResponse;
import com.addiction.user.userCigaretteHistory.service.response.UserCigaretteHistoryGraphPatientResponse;
import com.addiction.user.userCigaretteHistory.service.response.UserCigaretteHistoryGraphResponse;
import com.addiction.user.userCigaretteHistory.service.response.UserCigaretteHistoryResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCigaretteHistoryServiceImpl implements UserCigaretteHistoryService {

	private final SecurityService securityService;

	private final UserCigaretteHistoryRepository userCigaretteHistoryRepository;

	@Override
	public void save(String monthStr, String dateStr, int userId, int smokeCount, long avgPatienceTime,
		List<CigaretteHistoryDocument.History> historyList) {
		CigaretteHistoryDocument doc = CigaretteHistoryDocument.builder()
			.month(monthStr)
			.date(dateStr)
			.userId(userId)
			.smokeCount(smokeCount)
			.avgPatienceTime(avgPatienceTime)
			.history(historyList)
			.build();

		userCigaretteHistoryRepository.save(doc);
	}

	@Override
	public List<UserCigaretteHistoryCalenderResponse> findCalendarByDate(String month) {
		int userId = securityService.getCurrentLoginUserInfo().getUserId();
		return userCigaretteHistoryRepository.findByMonthAndUserId(month, userId).stream()
			.map(doc -> UserCigaretteHistoryCalenderResponse.createResponse(doc.getDate(), doc.getSmokeCount()))
			.collect(Collectors.toList());
	}

	@Override
	public List<UserCigaretteHistoryResponse> findHistoryByDate(String date) {
		int userId = securityService.getCurrentLoginUserInfo().getUserId();

		return userCigaretteHistoryRepository.findByDateAndUserId(date, userId).getHistory()
			.stream().map(UserCigaretteHistoryResponse::createResponse).toList();
	}

	public UserCigaretteHistoryGraphResponse findGraphByPeriod(PeriodType periodType) {
		int userId = securityService.getCurrentLoginUserInfo().getUserId();
		LocalDate endDate = LocalDate.now();
		LocalDate startDate = periodType.calculateStartDate(endDate);

		String start = startDate.format(DateTimeFormatter.BASIC_ISO_DATE);
		String end = endDate.format(DateTimeFormatter.BASIC_ISO_DATE);

		List<CigaretteHistoryDocument> docs = userCigaretteHistoryRepository.findByUserIdAndDateBetween(userId, start,
			end);

		return UserCigaretteHistoryGraphResponse.createResponse(
			createGraphCountResponse(docs),
			createGraphPatientResponse(docs)
		);
	}

	private UserCigaretteHistoryGraphCountResponse createGraphCountResponse(List<CigaretteHistoryDocument> docs) {
		List<UserCigaretteHistoryGraphDateResponse> dateList = docs.stream()
			.map(doc -> UserCigaretteHistoryGraphDateResponse.createResponse(doc.getDate(), doc.getSmokeCount()))
			.toList();

		int avgCigaretteCount = dateList.isEmpty() ? 0 :
			(int)Math.round(
				dateList.stream().mapToLong(UserCigaretteHistoryGraphDateResponse::getValue).average().orElse(0));

		return UserCigaretteHistoryGraphCountResponse.createResponse(
			avgCigaretteCount,
			dateList
		);
	}

	private UserCigaretteHistoryGraphPatientResponse createGraphPatientResponse(List<CigaretteHistoryDocument> docs) {
		List<UserCigaretteHistoryGraphDateResponse> dateList = docs.stream()
			.map(doc -> UserCigaretteHistoryGraphDateResponse.createResponse(doc.getDate(), doc.getAvgPatienceTime()))
			.collect(Collectors.toList());

		long avgSmokePatientTime = dateList.isEmpty() ? 0 :
			Math.round(dateList.stream().mapToLong(UserCigaretteHistoryGraphDateResponse::getValue).average().orElse(0));

		return UserCigaretteHistoryGraphPatientResponse.createResponse(
			avgSmokePatientTime,
			dateList
		);
	}
}
