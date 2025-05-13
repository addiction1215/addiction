package com.addiction.user.users.controller.request;

import com.addiction.user.users.service.request.UserUpdateServiceRequest;
import com.addiction.user.users.entity.enums.Sex;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequest {

	@NotNull(message = "성별은 필수입니다.")
	private Sex sex;
	@NotNull(message = "생년월일은 필수입니다.")
	private String birthDay;

	@Builder
	public UserUpdateRequest(Sex sex, String birthDay) {
		this.sex = sex;
		this.birthDay = birthDay;
	}

	public UserUpdateServiceRequest toServiceRequest() {
		return UserUpdateServiceRequest.builder()
			.sex(sex)
			.birthDay(birthDay)
			.build();
	}
}
