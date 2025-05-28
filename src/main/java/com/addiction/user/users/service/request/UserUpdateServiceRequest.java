package com.addiction.user.users.service.request;

import com.addiction.user.users.entity.enums.Sex;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class UserUpdateServiceRequest {

	private final String phoneNumber;
	private final Sex sex;
	private final String birthDay;

	@Builder
	public UserUpdateServiceRequest(String phoneNumber, Sex sex, String birthDay) {
		this.phoneNumber = phoneNumber;
		this.sex = sex;
		this.birthDay = birthDay;
	}
}
