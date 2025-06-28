package com.addiction.user.userCigarette.service.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserCigaretteChangeServiceRequest {

	private final ChangeType changeType;
	private final String address;

	@Builder
	public UserCigaretteChangeServiceRequest(ChangeType changeType, String address) {
		this.changeType = changeType;
		this.address = address;
	}

}
