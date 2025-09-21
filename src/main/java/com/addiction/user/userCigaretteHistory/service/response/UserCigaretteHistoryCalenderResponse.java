package com.addiction.user.userCigaretteHistory.service.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserCigaretteHistoryCalenderResponse {

	private final String date;
	private final int count;

	@Builder
	public UserCigaretteHistoryCalenderResponse(String date, int count) {
		this.date = date;
		this.count = count;
	}

	public static UserCigaretteHistoryCalenderResponse createResponse(String date, int count) {
		return new UserCigaretteHistoryCalenderResponse(date, count);
	}

}
