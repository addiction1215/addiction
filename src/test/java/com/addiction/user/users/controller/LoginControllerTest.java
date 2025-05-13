package com.addiction.user.users.controller;

import static org.springframework.http.MediaType.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import com.addiction.ControllerTestSupport;
import com.addiction.user.users.controller.request.LoginOauthRequest;
import com.addiction.user.users.controller.request.UserSaveRequest;
import com.addiction.user.users.entity.enums.SnsType;

public class LoginControllerTest extends ControllerTestSupport {

	@DisplayName("OAuth 로그인을 한다.")
	@Test
	@WithMockUser(roles = "USER")
	void oauth_로그인한다() throws Exception {
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

	@DisplayName("OAuth 로그인을 할 시 snsType값은 필수이다.")
	@Test
	@WithMockUser(roles = "USER")
	void oauth_로그인을_할_시_snsType값은_필수이다() throws Exception {
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

	@DisplayName("OAuth 로그인을 할 시 token값은 필수이다.")
	@Test
	@WithMockUser(roles = "USER")
	void oauth_로그인을_할_시_token값은_필수이다() throws Exception {
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

	@DisplayName("OAuth 로그인을 할 시 DeviceId값은 필수이다.")
	@Test
	@WithMockUser(roles = "USER")
	void oauth_로그인을_할_시_DeviceId값은_필수이다() throws Exception {
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

	@DisplayName("사용자 정보를 저장한다.")
	@Test
	@WithMockUser(roles = "USER")
	void 사용자_정보를_저장한다() throws Exception {
		// given
		UserSaveRequest request = UserSaveRequest.builder()
			.email("test@test.com")
			.password("1234")
			.phoneNumber("01012341234")
			.build();

		// when // then
		mockMvc.perform(
				post("/api/v1/auth/join")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.statusCode").value("201"))
			.andExpect(jsonPath("$.httpStatus").value("CREATED"))
			.andExpect(jsonPath("$.message").value("CREATED"));
	}

	@DisplayName("사용자 정보를 저장시 이메일은 필수입니다.")
	@Test
	@WithMockUser(roles = "USER")
	void 사용자_정보를_저장시_이메일은_필수입니다() throws Exception {
		// given
		UserSaveRequest request = UserSaveRequest.builder()
			.password("1234")
			.phoneNumber("01012341234")
			.build();

		// when // then
		mockMvc.perform(
				post("/api/v1/auth/join")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.statusCode").value("400"))
			.andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("이메일은 필수입니다."));
	}

	@DisplayName("사용자 정보를 저장시 비밀번호는 필수입니다.")
	@Test
	@WithMockUser(roles = "USER")
	void 사용자_정보를_저장시_비밀번호는_필수입니다() throws Exception {
		// given
		UserSaveRequest request = UserSaveRequest.builder()
			.email("test@test.com")
			.phoneNumber("01012341234")
			.build();

		// when // then
		mockMvc.perform(
				post("/api/v1/auth/join")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.statusCode").value("400"))
			.andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("비밀번호는 필수입니다."));
	}

	@DisplayName("사용자 정보를 저장시 핸드폰번호는 필수이다.")
	@Test
	@WithMockUser(roles = "USER")
	void 사용자_정보를_저장시_핸드폰번호는_필수이다() throws Exception {
		// given
		UserSaveRequest request = UserSaveRequest.builder()
			.email("test@test.com")
			.password("1234")
			.build();

		// when // then
		mockMvc.perform(
				post("/api/v1/auth/join")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.statusCode").value("400"))
			.andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("핸드폰번호는 필수입니다."));
	}
}
