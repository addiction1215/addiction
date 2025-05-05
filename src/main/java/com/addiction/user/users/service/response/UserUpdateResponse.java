package com.addiction.user.users.service.response;

import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.Sex;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateResponse {

	private Sex sex;
	private String birthDay;

	@Builder
	public UserUpdateResponse(Sex sex, String birthDay) {
		this.sex = sex;
		this.birthDay = birthDay;
	}

	public static UserUpdateResponse createResponse(User user) {
		return UserUpdateResponse.builder()
			.sex(user.getSex())
			.birthDay(user.getBirthDay())
			.build();
	}
}
