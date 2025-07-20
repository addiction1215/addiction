package com.addiction.user.userCigaretteHistory.service.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserCigaretteHistoryGraphDateResponse {

	private final String date;
	private final long value;

	@Builder
	public UserCigaretteHistoryGraphDateResponse(String date, long value) {
		this.date = date;
		this.value = value;
	}

	public static UserCigaretteHistoryGraphDateResponse createResponse(String date, long value) {
		return UserCigaretteHistoryGraphDateResponse.builder()
			.date(date)
			.value(value)
			.build();
	}
}
