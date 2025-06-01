package com.addiction.user.users.service.response;

import com.addiction.user.users.entity.User;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class UserSaveResponse {

	private final String email;
	private final String phoneNumber;

	@Builder
	public UserSaveResponse(String email, String phoneNumber) {
		this.email = email;
		this.phoneNumber = phoneNumber;
	}

	public static UserSaveResponse createResponse(User user) {
		return UserSaveResponse.builder()
			.email(user.getEmail())
			.phoneNumber(user.getPhoneNumber())
			.build();
	}
}
