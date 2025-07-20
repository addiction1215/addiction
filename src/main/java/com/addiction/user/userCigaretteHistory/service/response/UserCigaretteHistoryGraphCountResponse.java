package com.addiction.user.userCigaretteHistory.service.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserCigaretteHistoryGraphCountResponse {

	private final int avgCigaretteCount;
	private final List<UserCigaretteHistoryGraphDateResponse> date;

	@Builder
	public UserCigaretteHistoryGraphCountResponse(int avgCigaretteCount, List<UserCigaretteHistoryGraphDateResponse> date) {
		this.avgCigaretteCount = avgCigaretteCount;
		this.date = date;
	}

	public static UserCigaretteHistoryGraphCountResponse createResponse(int avgCigaretteCount,
		List<UserCigaretteHistoryGraphDateResponse> date) {
		return UserCigaretteHistoryGraphCountResponse.builder()
			.avgCigaretteCount(avgCigaretteCount)
			.date(date)
			.build();
	}
}
