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
import com.addiction.user.users.dto.controller.request.UserSaveRequest;
import com.addiction.user.users.dto.controller.request.UserUpdateRequest;
import com.addiction.user.users.entity.enums.Sex;

public class UserControllerTest extends ControllerTestSupport {

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
				post("/api/v1/user")
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
				post("/api/v1/user")
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
				post("/api/v1/user")
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
				post("/api/v1/user")
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

	@DisplayName("사용자 정보를 수정한다.")
	@Test
	@WithMockUser(roles = "USER")
	void 사용자_정보를_수정한다() throws Exception {
		// given
		UserUpdateRequest request = UserUpdateRequest.builder()
			.sex(Sex.MAIL)
			.birthDay("12341234")
			.build();

		// when // then
		mockMvc.perform(
				patch("/api/v1/user")
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

	@DisplayName("사용자 정보를 수정시 성별은 필수이다.")
	@Test
	@WithMockUser(roles = "USER")
	void 사용자_정보를_수정시_성별은_필수이다() throws Exception {
		// given
		UserUpdateRequest request = UserUpdateRequest.builder()
			.birthDay("12341234")
			.build();

		// when // then
		mockMvc.perform(
				patch("/api/v1/user")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.statusCode").value("400"))
			.andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("성별은 필수입니다."));
	}

	@DisplayName("사용자 정보를 수정시 생년월일은 필수이다.")
	@Test
	@WithMockUser(roles = "USER")
	void 사용자_정보를_수정시_생년월일은_필수이다() throws Exception {
		// given
		UserUpdateRequest request = UserUpdateRequest.builder()
			.sex(Sex.MAIL)
			.build();

		// when // then
		mockMvc.perform(
				patch("/api/v1/user")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.statusCode").value("400"))
			.andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("생년월일은 필수입니다."));
	}
}
