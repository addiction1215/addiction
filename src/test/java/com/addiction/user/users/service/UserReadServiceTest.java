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
}
