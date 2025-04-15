package com.addiction.user.users.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.addiction.IntegrationTestSupport;
import com.addiction.user.push.repository.PushRepository;
import com.addiction.user.refreshToken.repository.RefreshTokenRepository;
import com.addiction.user.users.dto.service.request.OAuthLoginServiceRequest;
import com.addiction.user.users.dto.service.response.OAuthLoginResponse;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;
import com.addiction.user.users.oauth.feign.google.response.GoogleUserInfoResponse;
import com.addiction.user.users.oauth.feign.kakao.response.KakaoUserInfoResponse;
import com.addiction.user.users.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;

public class LoginServiceTest extends IntegrationTestSupport {

	@Autowired
	private LoginService loginService;
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PushRepository pushRepository;

	@AfterEach
	void tearDown() {
		refreshTokenRepository.deleteAllInBatch();
		pushRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}

	@DisplayName("카카오 로그인을 한다.")
	@Test
	@Transactional
	void 카카오_로그인을_한다() throws JsonProcessingException {
		// given
		User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);

		userRepository.save(user);

		given(kakaoApiFeignCall.getUserInfo(any(String.class)))
			.willReturn(
				KakaoUserInfoResponse.builder()
					.kakaoAccount(KakaoUserInfoResponse.KakaoAccount.builder()
						.email("test@test.com")
						.build())
					.build()
			);

		OAuthLoginServiceRequest oAuthLoginServiceRequest = OAuthLoginServiceRequest.builder()
			.token("sadhAewofneonfoweifkpowekfkajfbdsnflksndfdsmfkl")
			.deviceId("testDeviceId")
			.pushKey("testPushKey")
			.snsType(SnsType.KAKAO)
			.build();

		OAuthLoginResponse oAuthLoginResponse = loginService.oauthLogin(oAuthLoginServiceRequest);

		assertAll(
			() -> assertThat(oAuthLoginResponse.getAccessToken()).isNotNull(),
			() -> assertThat(oAuthLoginResponse.getRefreshToken()).isNotNull(),
			() -> assertThat(oAuthLoginResponse.getEmail()).isEqualTo("test@test.com")
		);
	}

	@DisplayName("구글 로그인을 한다.")
	@Test
	@Transactional
	void 구글_로그인을_한다() throws JsonProcessingException {
		// given
		User user = createUser("test@test.com", "1234", SnsType.GOOGLE, SettingStatus.INCOMPLETE);

		userRepository.save(user);

		given(googleApiFeignCall.getUserInfo(any(String.class)))
			.willReturn(
				GoogleUserInfoResponse.builder()
					.email("test@test.com")
					.build()
			);

		OAuthLoginServiceRequest oAuthLoginServiceRequest = OAuthLoginServiceRequest.builder()
			.token("sadhAewofneonfoweifkpowekfkajfbdsnflksndfdsmfkl")
			.deviceId("testDeviceId")
			.pushKey("testPushKey")
			.snsType(SnsType.GOOGLE)
			.build();

		OAuthLoginResponse oAuthLoginResponse = loginService.oauthLogin(oAuthLoginServiceRequest);

		assertAll(
			() -> assertThat(oAuthLoginResponse.getAccessToken()).isNotNull(),
			() -> assertThat(oAuthLoginResponse.getRefreshToken()).isNotNull(),
			() -> assertThat(oAuthLoginResponse.getEmail()).isEqualTo("test@test.com")
		);
	}

	@DisplayName("카카오 로그인을 할 시 이미 등록되어있는 이메일이라면 예외가 발생한다.")
	@Test
	@Transactional
	void 카카오_로그인을_할_시_이미_등록되어있는_이메일이라면_예외가_발생한다() throws JsonProcessingException {
		// given
		given(kakaoApiFeignCall.getUserInfo(any(String.class)))
			.willReturn(
				KakaoUserInfoResponse.builder()
					.kakaoAccount(KakaoUserInfoResponse.KakaoAccount.builder()
						.email("test@test.com")
						.build())
					.build()
			);

		OAuthLoginServiceRequest oAuthLoginServiceRequest = OAuthLoginServiceRequest.builder()
			.token("sadhAewofneonfoweifkpowekfkajfbdsnflksndfdsmfkl")
			.deviceId("testDeviceId")
			.pushKey("testPushKey")
			.snsType(SnsType.KAKAO)
			.build();

		OAuthLoginResponse oAuthLoginResponse = loginService.oauthLogin(oAuthLoginServiceRequest);

		assertAll(
			() -> assertThat(oAuthLoginResponse.getAccessToken()).isNotNull(),
			() -> assertThat(oAuthLoginResponse.getRefreshToken()).isNotNull(),
			() -> assertThat(oAuthLoginResponse.getEmail()).isEqualTo("test@test.com")
		);
	}
}
