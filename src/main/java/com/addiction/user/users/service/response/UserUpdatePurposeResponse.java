package com.addiction.user.users.service.response;

import com.addiction.user.users.entity.User;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserUpdatePurposeResponse {

	private final String purpose;

	@Builder
	public UserUpdatePurposeResponse(String purpose) {
		this.purpose = purpose;
	}

	public static UserUpdatePurposeResponse createResponse(User user) {
		return UserUpdatePurposeResponse.builder()
			.purpose(user.getPurpose())
			.build();
	}

}
