package com.addiction.user.users.controller.request;

import com.addiction.user.users.service.request.UserUpdatePurposeServiceRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdatePurposeRequest {

	@NotNull(message = "금연 목표는 필수입니다.")
	private String purpose;

	@Builder
	public UserUpdatePurposeRequest(String purpose) {
		this.purpose = purpose;
	}

	public UserUpdatePurposeServiceRequest toServiceRequest() {
		return UserUpdatePurposeServiceRequest.builder().purpose(purpose).build();
	}
}
