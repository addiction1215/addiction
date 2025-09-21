package com.addiction.user.userCigarette.controller.request;

import com.addiction.user.userCigarette.service.request.ChangeType;
import com.addiction.user.userCigarette.service.request.UserCigaretteChangeServiceRequest;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCigaretteChangeRequest {

	private ChangeType changeType;
	private String address;

	@Builder
	public UserCigaretteChangeRequest(ChangeType changeType, String address) {
		this.changeType = changeType;
		this.address = address;
	}

	public UserCigaretteChangeServiceRequest toServiceRequest() {
		return UserCigaretteChangeServiceRequest.builder()
			.changeType(changeType)
			.address(address)
			.build();
	}


}
