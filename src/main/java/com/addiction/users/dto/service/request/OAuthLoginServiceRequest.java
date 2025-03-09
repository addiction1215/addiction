package com.addiction.users.dto.service.request;

import com.addiction.users.entity.SnsType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OAuthLoginServiceRequest {
	private String token;
	private SnsType snsType;
	private String deviceId;
	private String pushKey;

	@Builder
	private OAuthLoginServiceRequest(String token, SnsType snsType, String deviceId, String pushKey) {
		this.token = token;
		this.snsType = snsType;
		this.deviceId = deviceId;
		this.pushKey = pushKey;
	}
}
