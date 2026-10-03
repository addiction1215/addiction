package com.addiction.user.users.service.response;

import com.addiction.user.users.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserPurposeResponse {

	@Schema(description = "사용자가 설정한 금연 목표", example = "건강을 위해 금연하기")
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
