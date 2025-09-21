package com.addiction.user.users.service.response;

import com.addiction.user.users.entity.User;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserPurposeResponse {

	private final String purpose;

	@Builder
	public UserPurposeResponse(String purpose) {
		this.purpose = purpose;
	}

	public static UserPurposeResponse createResponse(User user) {
		return UserPurposeResponse.builder()
			.purpose(user.getPurpose())
			.build();
	}
}
