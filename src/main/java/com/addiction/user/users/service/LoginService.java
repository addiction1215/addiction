package com.addiction.user.users.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.addiction.global.exception.AddictionException;
import com.addiction.jwt.JwtTokenGenerator;
import com.addiction.jwt.dto.JwtToken;
import com.addiction.jwt.dto.LoginUserInfo;
import com.addiction.user.users.dto.service.request.OAuthLoginServiceRequest;
import com.addiction.user.users.dto.service.response.OAuthLoginResponse;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.Role;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;
import com.addiction.user.users.oauth.client.OAuthApiClient;
import com.addiction.user.users.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LoginService {

	private final UserRepository userRepository;
	private final JwtTokenGenerator jwtTokenGenerator;
	private final Map<SnsType, OAuthApiClient> clients;

	public LoginService(UserRepository userRepository, JwtTokenGenerator jwtTokenGenerator,
		List<OAuthApiClient> clients) {
		this.userRepository = userRepository;
		this.jwtTokenGenerator = jwtTokenGenerator;
		this.clients = clients.stream().collect(
			Collectors.toUnmodifiableMap(OAuthApiClient::oAuthSnsType, Function.identity())
		);
	}

	public OAuthLoginResponse oauthLogin(OAuthLoginServiceRequest oAuthLoginServiceRequest) throws
		JsonProcessingException {
		SnsType snsType = oAuthLoginServiceRequest.getSnsType();

		OAuthApiClient client = clients.get(snsType);
		Optional.ofNullable(client).orElseThrow(() -> new AddictionException("존재하지않는 로그인방식입니다."));

		String email = client.getEmail(oAuthLoginServiceRequest.getToken());

		User user;
		try {
			user = userRepository.findByEmail(email);
		} catch (AddictionException e) {
			user = userRepository.save(
				User.builder()
					.email(email)
					.snsType(snsType)
					.role(Role.USER)
					.settingStatus(SettingStatus.INCOMPLETE)
					.build()
			);
		}

		user.checkSnsType(snsType);              //SNS가입여부확인

		JwtToken jwtToken = setJwtTokenPushKey(user, oAuthLoginServiceRequest.getDeviceId(),
			oAuthLoginServiceRequest.getPushKey());

		return OAuthLoginResponse.of(user, jwtToken);
	}

	private JwtToken setJwtTokenPushKey(User user, String deviceId, String pushKey) throws JsonProcessingException {
		LoginUserInfo userInfo = LoginUserInfo.of(user.getId());
		JwtToken jwtToken = jwtTokenGenerator.generate(userInfo);
		user.checkRefreshToken(jwtToken, deviceId);
		user.checkPushKey(pushKey, deviceId);
		return jwtToken;
	}

}
