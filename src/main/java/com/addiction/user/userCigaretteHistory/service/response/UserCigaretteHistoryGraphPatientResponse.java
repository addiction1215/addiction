package com.addiction.user.userCigaretteHistory.service.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserCigaretteHistoryGraphPatientResponse {

	private final long avgSmokePatientTime;
	private final List<UserCigaretteHistoryGraphDateResponse> date;

	@Builder
	public UserCigaretteHistoryGraphPatientResponse(long avgSmokePatientTime, List<UserCigaretteHistoryGraphDateResponse> date) {
		this.avgSmokePatientTime = avgSmokePatientTime;
		this.date = date;
	}

	public static UserCigaretteHistoryGraphPatientResponse createResponse(long avgSmokePatientTime,
		List<UserCigaretteHistoryGraphDateResponse> date) {
		return UserCigaretteHistoryGraphPatientResponse.builder()
			.avgSmokePatientTime(avgSmokePatientTime)
			.date(date)
			.build();
	}
}
