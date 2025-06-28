package com.addiction.user.userCigarette.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.addiction.IntegrationTestSupport;
import com.addiction.user.userCigarette.service.request.ChangeType;
import com.addiction.user.userCigarette.service.request.UserCigaretteChangeServiceRequest;
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

		UserCigaretteChangeServiceRequest userCigaretteChangeServiceRequest = UserCigaretteChangeServiceRequest.builder()
			.changeType(ChangeType.ADD)
			.address("서울시 강남구 역삼동")
			.build();

		// when
		// then
		userCigaretteService.changeCigarette(userCigaretteChangeServiceRequest);

		assertThat(userCigaretteRepository.findAll())
			.hasSize(1)
			.extracting("address")
			.containsExactly("서울시 강남구 역삼동");
	}

	@DisplayName("유저의 흡연 갯수를 감소한다")
	@Test
	@Transactional
	void 유저의_흡연_갯수를_감소한다() {
		// given
		User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
		userRepository.save(user);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		UserCigaretteChangeServiceRequest userCigaretteChangeServiceRequest = UserCigaretteChangeServiceRequest.builder()
			.changeType(ChangeType.ADD)
			.address("서울시 강남구 역삼동")
			.build();

		UserCigaretteChangeServiceRequest userCigaretteChangeServiceMinusRequest = UserCigaretteChangeServiceRequest.builder()
			.changeType(ChangeType.MINUS)
			.build();

		// when
		// then
		userCigaretteService.changeCigarette(userCigaretteChangeServiceRequest);
		userCigaretteService.changeCigarette(userCigaretteChangeServiceMinusRequest);
		assertThat(userCigaretteRepository.findAll())
			.hasSize(0);
	}
}
