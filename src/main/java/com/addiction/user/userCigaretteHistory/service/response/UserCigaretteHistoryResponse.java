package com.addiction.user.userCigaretteHistory.service.response;

import com.addiction.user.userCigaretteHistory.document.CigaretteHistoryDocument;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserCigaretteHistoryResponse {

	private final String address;
	private final long smokePatienceTime;
	private final LocalDateTime smokeTime;

	@Builder
	public UserCigaretteHistoryResponse(String address, long smokePatienceTime, LocalDateTime smokeTime) {
		this.address = address;
		this.smokePatienceTime = smokePatienceTime;
		this.smokeTime = smokeTime;
	}

	public static UserCigaretteHistoryResponse createResponse(CigaretteHistoryDocument.History history) {
		return UserCigaretteHistoryResponse.builder()
			.address(history.getAddress())
			.smokePatienceTime(history.getSmokePatienceTime())
			.smokeTime(history.getSmokeTime())
			.build();
	}
}
