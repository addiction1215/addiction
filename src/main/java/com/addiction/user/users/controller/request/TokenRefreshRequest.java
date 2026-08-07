package com.addiction.user.users.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenRefreshRequest {
	@NotBlank(message = "Refresh token은 필수입니다.")
	private String refreshToken;

	@NotBlank(message = "디바이스ID는 필수입니다.")
	private String deviceId;
}
