package com.addiction.user.users.dto.service.response;

import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.Sex;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateResponse {

	private String phoneNumber;
	private Sex sex;
	private String birthDay;

	@Builder
	public UserUpdateResponse(String phoneNumber, Sex sex, String birthDay) {
		this.phoneNumber = phoneNumber;
		this.sex = sex;
		this.birthDay = birthDay;
	}

	public static UserUpdateResponse createResponse(User user) {
		return UserUpdateResponse.builder()
			.phoneNumber(user.getPhoneNumber())
			.sex(user.getSex())
			.birthDay(user.getBirthDay())
			.build();
	}
}
