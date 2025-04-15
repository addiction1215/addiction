package com.addiction.user.users.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.addiction.IntegrationTestSupport;
import com.addiction.user.users.dto.service.request.UserSaveServiceRequest;
import com.addiction.user.users.dto.service.request.UserUpdateServiceRequest;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.Sex;
import com.addiction.user.users.entity.enums.SnsType;

public class UserServiceTest extends IntegrationTestSupport {

	@Autowired
	private UserService userService;

	@DisplayName("유저의 정보를 저장한다.")
	@Test
	void 유저의_정보를_저장한다() {
		//given
		UserSaveServiceRequest userSaveServiceRequest = UserSaveServiceRequest.builder()
			.email("test@test.com")
			.password("1234")
			.phoneNumber("01012341234")
			.build();

		//when
		userService.save(userSaveServiceRequest);

		//then
		assertThat(userRepository.findByEmail("test@test.com"))
			.extracting("email", "phoneNumber")
			.contains("test@test.com", "01012341234");
	}

	@DisplayName("유저의 정보를 수정한다.")
	@Test
	void 유저의_정보를_수정한다() {
		//given
		User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);

		User savedUser = userRepository.save(user);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(savedUser.getId()));

		UserUpdateServiceRequest userUpdateServiceRequest = UserUpdateServiceRequest.builder()
			.sex(Sex.MAIL)
			.birthDay("12341234")
			.build();

		//when
		userService.update(userUpdateServiceRequest);

		//then
		assertThat(userRepository.findById(savedUser.getId()))
			.extracting("sex", "birthDay")
			.contains(Sex.MAIL, "12341234");
	}
}
