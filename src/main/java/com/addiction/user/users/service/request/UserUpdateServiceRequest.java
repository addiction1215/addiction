package com.addiction.user.users.service.request;

import com.addiction.user.users.entity.enums.Sex;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateServiceRequest {

	private String phoneNumber;
	private Sex sex;
	private String birthDay;

	@Builder
	public UserUpdateServiceRequest(String phoneNumber, Sex sex, String birthDay) {
		this.phoneNumber = phoneNumber;
		this.sex = sex;
		this.birthDay = birthDay;
	}
}
