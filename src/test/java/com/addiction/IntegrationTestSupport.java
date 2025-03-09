package com.addiction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.addiction.users.entity.SettingStatus;
import com.addiction.users.entity.SnsType;
import com.addiction.users.entity.User;
import com.addiction.users.oauth.feign.google.GoogleApiFeignCall;
import com.addiction.users.oauth.feign.kakao.KakaoApiFeignCall;

@ActiveProfiles("test")
@SpringBootTest
public abstract class IntegrationTestSupport {

	@MockitoBean
	protected KakaoApiFeignCall kakaoApiFeignCall;
	@MockitoBean
	protected GoogleApiFeignCall googleApiFeignCall;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	protected User createUser(String email, String password, SnsType snsType, SettingStatus settingStatus) {
		return User.builder()
			.email(email)
			.password(bCryptPasswordEncoder.encode(password))
			.snsType(snsType)
			.settingStatus(settingStatus)
			.build();
	}
}
