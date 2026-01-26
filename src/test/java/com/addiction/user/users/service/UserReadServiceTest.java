package com.addiction.user.users.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.addiction.IntegrationTestSupport;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;
import com.addiction.user.users.repository.UserRepository;

import com.addiction.user.users.service.response.UserSimpleProfileResponse;

public class UserReadServiceTest extends IntegrationTestSupport {

	@Autowired
	private UserReadService userReadService;

	@Autowired
	private UserRepository userRepository;

	@DisplayName("유저의 목표를 조회한다.")
	@Test
	void 유저의_목표를_조회한다() {
		//given
		User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
		userRepository.save(user);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		//when
		//then
		assertThat(userReadService.findPurpose().getPurpose()).isEqualTo("테스트 목표");
	}

	@DisplayName("유저의 간단한 프로필 정보를 조회한다.")
	@Test
	void 유저의_간단한_프로필_정보를_조회한다() {
		//given
		User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
		user.updateProfileUrl("test.com");
		userRepository.save(user);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		//when
		UserSimpleProfileResponse response = userReadService.findSimpleProfile();

		//then
		assertThat(response.getEmail()).isEqualTo("test@test.com");
		assertThat(response.getNickName()).isEqualTo("테스트 닉네임");
	}
}
