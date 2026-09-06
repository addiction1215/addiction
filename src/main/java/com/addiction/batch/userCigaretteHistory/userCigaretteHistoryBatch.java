package com.addiction.batch.userCigaretteHistory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Clock;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
	private final UserReadService userReadService;
	private final Clock koreaClock;

	/**
	 * 전날의 실시간 흡연 원본(UserCigarette)을 사용자별 일별 통계(CigaretteHistoryDocument)로 이관한다.
	 * 저장된 일별 통계는 그래프의 기간별 평균과 대표 평균(avgSmokePatientTime) 계산에 사용된다.
	 * 이관이 완료되면 전날 원본 기록은 삭제하므로, 자정 이후에는 과거 기록을 MongoDB 통계에서 조회한다.
	 */
	@Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
	public void userCigaretteHistory() {
		// 한국 시간 자정에 실행되므로 집계 대상은 항상 전날 00:00:00 ~ 당일 00:00:00 직전이다.
		LocalDate yesterday = LocalDate.now(koreaClock).minusDays(1);
		String dateStr = yesterday.format(DateTimeFormatter.BASIC_ISO_DATE); // yyyyMMdd
		String monthStr = yesterday.format(DateTimeFormatter.ofPattern("yyyyMM")); // yyyyMM

		List<UserCigarette> allCigarettes = userCigaretteReadService.findAllByCreatedDateBetween(
			yesterday.atStartOfDay(), yesterday.plusDays(1).atStartOfDay());

		// 같은 날의 원본 흡연 기록을 사용자 단위로 묶어, 사용자별 일별 통계를 만든다.
		Map<Long, List<UserCigarette>> grouped = allCigarettes.stream()
			.collect(Collectors.groupingBy(c -> c.getUser().getId()));

		for (Map.Entry<Long, List<UserCigarette>> entry : grouped.entrySet()) {
			long userId = entry.getKey();
			List<UserCigarette> cigarettes = entry.getValue();
			try {
				int smokeCount = cigarettes.size();
				// [평균 참은 시간 계산 2/4] 하루 동안 기록된 흡연 간격(초)의 산술평균을
				// 일별 avgPatienceTime으로 저장한다. 예: 3,600초와 7,200초라면 5,400초다.
				// 총 참은 시간을 흡연 횟수로 나눈다. 정수 나눗셈이므로 소수점 이하는 버린다.
				long totalPatienceTime = cigarettes.stream()
					.mapToLong(UserCigarette::getSmokePatienceTime)
					.sum();
				long avgPatienceTime = totalPatienceTime / smokeCount;

				// 일별 통계와 함께 상세 조회에 사용할 흡연 시간, 장소, 참은 시간을 보관한다.
				List<CigaretteHistoryDocument.History> historyList = new ArrayList<>();
				for (UserCigarette cigarette : cigarettes) {
					historyList.add(CigaretteHistoryDocument.History.builder()
						.address(cigarette.getAddress())
						.smokeTime(cigarette.getSmokeTime())
						.smokePatienceTime(cigarette.getSmokePatienceTime())
						.build());
				}

				userCigaretteHistoryService.save(monthStr, dateStr, userId, smokeCount, avgPatienceTime, historyList);

			} catch (Exception e) {
				log.error("사용자 {}의 흡연 기록 배치 처리 중 오류 발생 - skip", userId, e);
			}
		}

		// [평균 참은 시간 계산 2/4] 어제 흡연 기록이 없는 활성 유저는
		// 해당 일의 avgPatienceTime을 86,400초(24시간)로 저장한다.
		for (User user : userReadService.findAll()) {
			if (!grouped.containsKey(user.getId())) {
				try {
					userCigaretteHistoryService.save(monthStr, dateStr, user.getId(), 0, SECONDS_PER_DAY, List.of());
				} catch (Exception e) {
					log.error("사용자 {}의 금연 기록 저장 중 오류 발생 - skip", user.getId(), e);
				}
			}
		}

		// 일별 통계 저장까지 끝난 전날 원본 기록은 중복 집계를 막기 위해 삭제한다.
		userCigaretteService.deleteAll(allCigarettes);
	}

}
