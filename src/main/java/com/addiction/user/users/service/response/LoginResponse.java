package com.addiction.user.users.service.response;

import com.addiction.jwt.dto.JwtToken;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SettingStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {
	private final String email;
	private final String accessToken;
	private final String refreshToken;
	private final SettingStatus settingStatus;

	@Builder
	private LoginResponse(String email, String accessToken, String refreshToken, SettingStatus settingStatus) {
		this.email = email;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.settingStatus = settingStatus;
	}

	public static LoginResponse of(User user, JwtToken jwtToken) {
		return LoginResponse.builder()
			.email(user.getEmail())
			.accessToken(jwtToken.getAccessToken())
			.refreshToken(jwtToken.getRefreshToken())
			.settingStatus(user.getSettingStatus())
			.build();
	}
}
