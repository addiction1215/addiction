package com.addiction.user.userCigarette.service.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserCigaretteFindResponse {

	private final int count;

	@Builder
	public UserCigaretteFindResponse(int count) {
		this.count = count;
	}

	public static UserCigaretteFindResponse createResponse(int count) {
		return UserCigaretteFindResponse.builder()
				.count(count)
				.build();
	}
}
