package com.addiction.user.users.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.addiction.global.exception.AddictionException;
import com.addiction.jwt.JwtTokenGenerator;
import com.addiction.jwt.dto.JwtToken;
import com.addiction.jwt.dto.LoginUserInfo;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.Role;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;
import com.addiction.user.users.oauth.client.OAuthApiClient;
import com.addiction.user.users.repository.UserRepository;
import com.addiction.user.users.service.LoginService;
import com.addiction.user.users.service.UserReadService;
import com.addiction.user.users.service.request.LoginServiceRequest;
import com.addiction.user.users.service.request.OAuthLoginServiceRequest;
import com.addiction.user.users.service.response.LoginResponse;
import com.addiction.user.users.service.response.OAuthLoginResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {

	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final JwtTokenGenerator jwtTokenGenerator;
	private final Map<SnsType, OAuthApiClient> clients;
	private final UserReadService userReadService;

	private final UserRepository userRepository;

	public LoginServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, JwtTokenGenerator jwtTokenGenerator,
		List<OAuthApiClient> clients, UserReadService userReadService, UserRepository userRepository) {
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.userRepository = userRepository;
		this.jwtTokenGenerator = jwtTokenGenerator;
		this.userReadService = userReadService;
		this.clients = clients.stream().collect(
			Collectors.toUnmodifiableMap(OAuthApiClient::oAuthSnsType, Function.identity())
		);
	}

	@Override
	public LoginResponse normalLogin(LoginServiceRequest loginServiceRequest) throws JsonProcessingException {
		User user = userReadService.findByEmail(loginServiceRequest.getEmail());     //1. 회원조회

		user.checkSnsType(SnsType.NORMAL);                                     //SNS가입여부확인

		if (!bCryptPasswordEncoder.matches(loginServiceRequest.getPassword(), user.getPassword())) {
			throw new AddictionException("아이디 또는 패스워드가 일치하지 않습니다.");
		} //3. 비밀번호 체크

		JwtToken jwtToken = setJwtTokenPushKey(user, loginServiceRequest.getDeviceId(),
			loginServiceRequest.getPushKey());

		return LoginResponse.of(user, jwtToken);
	}

	@Override
	public OAuthLoginResponse oauthLogin(OAuthLoginServiceRequest oAuthLoginServiceRequest) throws
		JsonProcessingException {
		SnsType snsType = oAuthLoginServiceRequest.getSnsType();

		OAuthApiClient client = clients.get(snsType);
		Optional.ofNullable(client).orElseThrow(() -> new AddictionException("존재하지않는 로그인방식입니다."));

		String email = client.getEmail(oAuthLoginServiceRequest.getToken());

		User user;
		try {
			user = userReadService.findByEmail(email);
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
