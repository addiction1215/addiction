package com.addiction.batch.userCigaretteHistory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.addiction.user.userCigarette.entity.UserCigarette;
import com.addiction.user.userCigarette.service.UserCigaretteReadService;
import com.addiction.user.userCigarette.service.UserCigaretteService;
import com.addiction.user.userCigaretteHistory.document.CigaretteHistoryDocument;
import com.addiction.user.userCigaretteHistory.service.UserCigaretteHistoryService;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.service.UserReadService;
import com.addiction.user.users.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class userCigaretteHistoryBatch {

	private static final long SECONDS_PER_DAY = 86400L;

	private final UserCigaretteHistoryService userCigaretteHistoryService;
	private final UserCigaretteReadService userCigaretteReadService;
	private final UserCigaretteService userCigaretteService;
	private final UserService userService;
	private final UserReadService userReadService;

	@Scheduled(cron = "0 0 0 * * *")
	public void userCigaretteHistory() {
		LocalDate yesterday = LocalDate.now().minusDays(1);
		String dateStr = yesterday.format(DateTimeFormatter.BASIC_ISO_DATE); // yyyyMMdd
		String monthStr = yesterday.format(DateTimeFormatter.ofPattern("yyyyMM")); // yyyyMM

		List<UserCigarette> allCigarettes = userCigaretteReadService.findAllByCreatedDateBetween(
			yesterday.atStartOfDay(), yesterday.plusDays(1).atStartOfDay());

		Map<Long, List<UserCigarette>> grouped = allCigarettes.stream()
			.collect(Collectors.groupingBy(c -> c.getUser().getId()));

		for (Map.Entry<Long, List<UserCigarette>> entry : grouped.entrySet()) {
			long userId = entry.getKey();
			List<UserCigarette> cigarettes = entry.getValue();
			try {
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

				// 마지막 흡연 시간으로 startDate 업데이트
				cigarettes.stream()
					.map(UserCigarette::getSmokeTime)
					.max(LocalDateTime::compareTo)
					.ifPresent(lastSmokeTime -> userService.updateStartDate(userId, lastSmokeTime));
			} catch (Exception e) {
				log.error("사용자 {}의 흡연 기록 배치 처리 중 오류 발생 - skip", userId, e);
			}
		}

		// 어제 흡연 기록이 없는 활성 유저는 금연시간 86400초(24시간)로 저장
		for (User user : userReadService.findAll()) {
			if (!grouped.containsKey(user.getId())) {
				try {
					userCigaretteHistoryService.save(monthStr, dateStr, user.getId(), 0, SECONDS_PER_DAY, List.of());
				} catch (Exception e) {
					log.error("사용자 {}의 금연 기록 저장 중 오류 발생 - skip", user.getId(), e);
				}
			}
		}

		userCigaretteService.deleteAll(allCigarettes);
	}
}
