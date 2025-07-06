package com.addiction.user.users.service.response;

import com.addiction.user.users.entity.User;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class UserSaveResponse {

	private final String email;
	private final String phoneNumber;
	private final String nickName;

	@Builder
	public UserSaveResponse(String email, String phoneNumber, String nickName) {
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.nickName = nickName;
	}

	public static UserSaveResponse createResponse(User user) {
		return UserSaveResponse.builder()
			.email(user.getEmail())
			.phoneNumber(user.getPhoneNumber())
			.nickName(user.getNickName())
			.build();
	}
}
