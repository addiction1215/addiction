package com.addiction.user.userCigaretteHistory.service.response;

import com.addiction.user.userCigaretteHistory.document.CigaretteHistoryDocument;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserCigaretteHistoryResponse {

	private final String address;
	private final long smokePatienceTime;

	@Builder
	public UserCigaretteHistoryResponse(String address, long smokePatienceTime) {
		this.address = address;
		this.smokePatienceTime = smokePatienceTime;
	}

	public static UserCigaretteHistoryResponse createResponse(CigaretteHistoryDocument.History history) {
		return UserCigaretteHistoryResponse.builder()
			.address(history.getAddress())
			.smokePatienceTime(history.getSmokePatienceTime())
			.build();
	}
}
