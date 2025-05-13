package com.addiction.user.users.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.addiction.global.ApiResponse;
import com.addiction.user.users.controller.request.LoginOauthRequest;
import com.addiction.user.users.controller.request.UserSaveRequest;
import com.addiction.user.users.service.UserService;
import com.addiction.user.users.service.response.OAuthLoginResponse;
import com.addiction.user.users.service.LoginService;
import com.addiction.user.users.service.response.UserSaveResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class LoginController {

	private final LoginService loginService;
	private final UserService userService;

	@PostMapping("/oauth/login")
	public ApiResponse<OAuthLoginResponse> oauthLogin(@Valid @RequestBody LoginOauthRequest loginOauthRequest) throws JsonProcessingException {
		return ApiResponse.ok(loginService.oauthLogin(loginOauthRequest.toServiceRequest()));
	}

	@PostMapping("/join")
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse<UserSaveResponse> save(@RequestBody @Valid UserSaveRequest userSaveRequest) {
		return ApiResponse.created(userService.save(userSaveRequest.toServiceRequest()));
	}

}
