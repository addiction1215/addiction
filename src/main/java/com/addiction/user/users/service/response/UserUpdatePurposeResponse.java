package com.addiction.user.users.service.response;

import com.addiction.user.users.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserUpdatePurposeResponse {

	@Schema(description = "수정된 금연 목표", example = "건강을 위해 금연하기")
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
