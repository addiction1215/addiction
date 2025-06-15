package com.addiction.user.userCigaretteHistory.service.request;

import com.addiction.user.users.entity.User;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserCigaretteHistoryServiceRequest {

	private final User user;
	private final int count;

	@Builder
	public UserCigaretteHistoryServiceRequest(User user, int count) {
		this.user = user;
		this.count = count;
	}

	public static UserCigaretteHistoryServiceRequest createRequest(User user, int count) {
		return UserCigaretteHistoryServiceRequest.builder()
			.user(user)
			.count(count)
			.build();
	}
}
