package com.addiction.user.userCigaretteHistory.service.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserCigaretteHistoryGraphResponse {

	private final UserCigaretteHistoryGraphCountResponse cigarette;
	private final UserCigaretteHistoryGraphPatientResponse patient;

	@Builder
	public UserCigaretteHistoryGraphResponse(UserCigaretteHistoryGraphCountResponse cigarette,
		UserCigaretteHistoryGraphPatientResponse patient) {
		this.cigarette = cigarette;
		this.patient = patient;
	}

	public static UserCigaretteHistoryGraphResponse createResponse(UserCigaretteHistoryGraphCountResponse cigarette,
		UserCigaretteHistoryGraphPatientResponse patient) {
		return UserCigaretteHistoryGraphResponse.builder()
			.cigarette(cigarette)
			.patient(patient)
			.build();
	}
}
