package com.addiction.user.users.service.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdatePurposeServiceRequest {

	private String purpose;

	@Builder
	public UserUpdatePurposeServiceRequest(String purpose) {
		this.purpose = purpose;
	}


}
