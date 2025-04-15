package com.addiction.user.users.dto.controller.request;

import com.addiction.user.users.dto.service.request.UserUpdateServiceRequest;
import com.addiction.user.users.entity.enums.Sex;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequest {

	private Long id;
	private String phoneNumber;
	private Sex sex;
	private String birthDay;

	@Builder
	public UserUpdateRequest(String phoneNumber, Sex sex, String birthDay) {
		this.phoneNumber = phoneNumber;
		this.sex = sex;
		this.birthDay = birthDay;
	}

	public UserUpdateServiceRequest toServiceRequest() {
		return UserUpdateServiceRequest.builder()
			.phoneNumber(phoneNumber)
			.sex(sex)
			.birthDay(birthDay)
			.build();
	}
}
