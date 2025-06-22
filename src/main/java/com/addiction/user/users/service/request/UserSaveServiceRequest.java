package com.addiction.user.users.service.request;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.Role;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class UserSaveServiceRequest {

	private final String email;
	private final String phoneNumber;
	private final String password;

	@Builder
	public UserSaveServiceRequest(String email, String phoneNumber, String password) {
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.password = password;
	}

	public User toEntity(BCryptPasswordEncoder bCryptPasswordEncoder) {
		return User.builder()
			.email(email)
			.password(bCryptPasswordEncoder.encode(password))
			.phoneNumber(phoneNumber)
			.snsType(SnsType.NORMAL)
			.role(Role.USER)
			.settingStatus(SettingStatus.INCOMPLETE)
			.build();
	}
}
