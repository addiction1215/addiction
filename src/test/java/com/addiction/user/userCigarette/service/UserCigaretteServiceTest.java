package com.addiction.user.userCigarette.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.addiction.IntegrationTestSupport;
import com.addiction.user.userCigarette.service.request.ChangeType;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;

public class UserCigaretteServiceTest extends IntegrationTestSupport {

	@Autowired
	private UserCigaretteService userCigaretteService;


	@DisplayName("유저의 흡연 갯수를 증가한다")
	@Test
	void 유저의_흡연_갯수를_증가한다() {
		// given
		User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
		userRepository.save(user);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		// when
		// then
		assertThat(userCigaretteService.changeCigaretteCount(ChangeType.ADD).getCount()).isEqualTo(1);
	}

	@DisplayName("유저의 흡연 갯수를 감소한다")
	@Test
	void 유저의_흡연_갯수를_감소한다() {
		// given
		User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
		userRepository.save(user);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		// when
		// then
		assertThat(userCigaretteService.changeCigaretteCount(ChangeType.MINUS).getCount()).isEqualTo(0);
	}
}
