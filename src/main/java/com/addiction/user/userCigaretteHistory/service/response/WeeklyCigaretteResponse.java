package com.addiction.user.userCigaretteHistory.service.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class WeeklyCigaretteResponse {

	private final List<DailyCigaretteCount> weekData;

	@Builder
	public WeeklyCigaretteResponse(List<DailyCigaretteCount> weekData) {
		this.weekData = weekData;
	}

	public static WeeklyCigaretteResponse createResponse(List<DailyCigaretteCount> weekData) {
		return WeeklyCigaretteResponse.builder()
				.weekData(weekData)
				.build();
	}

	@Getter
	public static class DailyCigaretteCount {
		private final String date;        // yyyyMMdd 형식
		private final String dayOfWeek;   // 요일 (SUN, MON, TUE, WED, THU, FRI, SAT)
		private final int count;          // 흡연 개피 수

		@Builder
		public DailyCigaretteCount(String date, String dayOfWeek, int count) {
			this.date = date;
			this.dayOfWeek = dayOfWeek;
			this.count = count;
		}

		public static DailyCigaretteCount createResponse(String date, String dayOfWeek, int count) {
			return DailyCigaretteCount.builder()
					.date(date)
					.dayOfWeek(dayOfWeek)
					.count(count)
					.build();
		}
	}
}
