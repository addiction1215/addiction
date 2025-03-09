package com.addiction.users.controller;

import static org.springframework.http.MediaType.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import com.addiction.ControllerTestSupport;
import com.addiction.users.dto.controller.request.LoginOauthRequest;
import com.addiction.users.entity.SnsType;

public class LoginControllerTest extends ControllerTestSupport {

	@DisplayName("OAuth로그인을 한다.")
	@Test
	@WithMockUser(roles = "USER")
	void OauthLoginTest() throws Exception {
		// given
		LoginOauthRequest request = LoginOauthRequest.builder()
			.token("testToken")
			.snsType(SnsType.KAKAO)
			.pushKey("testPushKey")
			.deviceId("testdeviceId")
			.build();

		// when // then
		mockMvc.perform(
				post("/api/v1/auth/oauth/login")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value("200"))
			.andExpect(jsonPath("$.httpStatus").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"));
	}


	@DisplayName("OAuth로그인을 할 시 snsType값은 필수이다.")
	@Test
	@WithMockUser(roles = "USER")
	void OauthLoginWithoutTokenTest() throws Exception {
		// given
		LoginOauthRequest request = LoginOauthRequest.builder()
			.token("testToken")
			.pushKey("testPushKey")
			.deviceId("testdeviceId")
			.build();

		// when // then
		mockMvc.perform(
				post("/api/v1/auth/oauth/login")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.statusCode").value("400"))
			.andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("인증타입은 필수입니다."));
	}

	@DisplayName("OAuth로그인을 할 시 token값은 필수이다.")
	@Test
	@WithMockUser(roles = "USER")
	void OauthLoginWithoutSnsTypeTest() throws Exception {
		// given
		LoginOauthRequest request = LoginOauthRequest.builder()
			.snsType(SnsType.KAKAO)
			.pushKey("testPushKey")
			.deviceId("testdeviceId")
			.build();

		// when // then
		mockMvc.perform(
				post("/api/v1/auth/oauth/login")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.statusCode").value("400"))
			.andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("Token은 필수입니다."));
	}

	@DisplayName("OAuth로그인을 할 시 DeviceId값은 필수이다.")
	@Test
	@WithMockUser(roles = "USER")
	void OauthLoginWithoutDeviceIdTest() throws Exception {
		// given
		LoginOauthRequest request = LoginOauthRequest.builder()
			.token("testToken")
			.snsType(SnsType.KAKAO)
			.pushKey("testPushKey")
			.build();

		// when // then
		mockMvc.perform(
				post("/api/v1/auth/oauth/login")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.statusCode").value("400"))
			.andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("디바이스ID는 필수입니다."));
	}
}
