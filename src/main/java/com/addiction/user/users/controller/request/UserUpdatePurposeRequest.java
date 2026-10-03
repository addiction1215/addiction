package com.addiction.user.users.controller.request;

import com.addiction.user.users.service.request.UserUpdatePurposeServiceRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdatePurposeRequest {

	@NotNull(message = "금연 목표는 필수입니다.")
	@Schema(description = "사용자가 설정할 금연 목표", example = "건강을 위해 금연하기")
	private String purpose;

	@Builder
	public UserUpdatePurposeRequest(String purpose) {
		this.purpose = purpose;
	}

	public UserUpdatePurposeServiceRequest toServiceRequest() {
		return UserUpdatePurposeServiceRequest.builder().purpose(purpose).build();
	}
}
