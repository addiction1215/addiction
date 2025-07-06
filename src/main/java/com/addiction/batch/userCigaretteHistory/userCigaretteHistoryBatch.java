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

		Map<Integer, List<UserCigarette>> grouped = userCigaretteReadService.findAllByCreatedDateBetween(
				yesterday.atStartOfDay(), yesterday.plusDays(1).atStartOfDay()).stream()
			.collect(Collectors.groupingBy(c -> c.getUser().getId()));

		for (Map.Entry<Integer, List<UserCigarette>> entry : grouped.entrySet()) {
			int userId = entry.getKey();
			List<CigaretteHistoryDocument.History> historyList = entry.getValue().stream()
				.map(c -> CigaretteHistoryDocument.History.builder()
					.address(c.getAddress())
					.smokeTime(c.getCreatedDate())
					.smokePatienceTime(c.getSmokePatienceTime())
					.build())
				.collect(Collectors.toList());

			userCigaretteHistoryService.save(dateStr, userId, historyList);
		}
	}
}
