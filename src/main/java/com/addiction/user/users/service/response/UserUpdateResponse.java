package com.addiction.user.users.service.response;

import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.Sex;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class UserUpdateResponse {

	@Schema(description = "저장된 사용자 성별", example = "MALE", allowableValues = {"FEMALE", "MALE"})
	private final Sex sex;
	@Schema(description = "저장된 생년월일 8자리 문자열", example = "19960101")
	private final String birthDay;

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
