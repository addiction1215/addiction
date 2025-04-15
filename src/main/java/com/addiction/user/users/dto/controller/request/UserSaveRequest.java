package com.addiction.user.users.dto.controller.request;

import com.addiction.user.users.dto.service.request.UserSaveServiceRequest;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.Role;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSaveRequest {

	@NotNull(message = "이메일은 필수입니다.")
	private String email;
	@NotNull(message = "핸드폰번호는 필수입니다.")
	private String phoneNumber;
	@NotNull(message = "비밀번호는 필수입니다.")
	private String password;

	@Builder
	public UserSaveRequest(String email, String phoneNumber, String password) {
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.password = password;
	}

	public UserSaveServiceRequest toServiceRequest() {
		return UserSaveServiceRequest.builder()
			.email(email)
			.password(password)
			.phoneNumber(phoneNumber)
			.build();
	}
}
