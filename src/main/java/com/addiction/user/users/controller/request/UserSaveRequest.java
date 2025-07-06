package com.addiction.user.users.controller.request;

import com.addiction.user.users.service.request.UserSaveServiceRequest;

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
	@NotNull(message = "닉네임은 필수입니다.")
	private String nickName;

	@Builder
	public UserSaveRequest(String email, String phoneNumber, String password, String nickName) {
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.nickName = nickName;
	}

	public UserSaveServiceRequest toServiceRequest() {
		return UserSaveServiceRequest.builder()
			.email(email)
			.password(password)
			.phoneNumber(phoneNumber)
			.nickName(nickName)
			.build();
	}
}
