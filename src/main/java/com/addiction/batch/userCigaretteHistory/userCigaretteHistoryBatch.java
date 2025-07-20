package com.addiction.batch.userCigaretteHistory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.addiction.user.userCigarette.entity.UserCigarette;
import com.addiction.user.userCigarette.service.UserCigaretteReadService;
import com.addiction.user.userCigaretteHistory.document.CigaretteHistoryDocument;
import com.addiction.user.userCigaretteHistory.service.UserCigaretteHistoryService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class userCigaretteHistoryBatch {

	private final UserCigaretteHistoryService userCigaretteHistoryService;
	private final UserCigaretteReadService userCigaretteReadService;

	@Scheduled(cron = "0 0 0 * * *")
	public void userCigaretteHistory() {
		LocalDate yesterday = LocalDate.now().minusDays(1);
		String dateStr = yesterday.format(DateTimeFormatter.BASIC_ISO_DATE); // yyyyMMdd
		String monthStr = yesterday.format(DateTimeFormatter.ofPattern("yyyyMM")); // yyyyMM

		Map<Integer, List<UserCigarette>> grouped = userCigaretteReadService.findAllByCreatedDateBetween(
				yesterday.atStartOfDay(), yesterday.plusDays(1).atStartOfDay()).stream()
			.collect(Collectors.groupingBy(c -> c.getUser().getId()));

		for (Map.Entry<Integer, List<UserCigarette>> entry : grouped.entrySet()) {
			int userId = entry.getKey();

			List<UserCigarette> cigarettes = entry.getValue();

			int smokeCount = cigarettes.size();
			long avgPatienceTime = (long) cigarettes.stream()
				.mapToLong(UserCigarette::getSmokePatienceTime)
				.average()
				.orElse(0);

			List<CigaretteHistoryDocument.History> historyList = cigarettes.stream()
				.map(c -> CigaretteHistoryDocument.History.builder()
					.address(c.getAddress())
					.smokeTime(c.getCreatedDate())
					.smokePatienceTime(c.getSmokePatienceTime())
					.build())
				.collect(Collectors.toList());

			userCigaretteHistoryService.save(monthStr, dateStr, userId, smokeCount, avgPatienceTime, historyList);
		}
	}
}
