package com.addiction.user.users.dto.service.response;

import com.addiction.user.users.entity.User;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserSaveResponse {

	private String email;
	private String phoneNumber;

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
