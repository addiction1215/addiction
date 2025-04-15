package com.addiction.user.users.controller;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.addiction.global.ApiResponse;
import com.addiction.user.users.dto.controller.request.UserUpdateRequest;
import com.addiction.user.users.dto.service.response.UserUpdateResponse;
import com.addiction.user.users.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

	private final UserService userService;

	@PatchMapping
	public ApiResponse<UserUpdateResponse> update(@RequestBody @Valid UserUpdateRequest userUpdateRequest) {
		return ApiResponse.ok(userService.update(userUpdateRequest.toServiceRequest()));
	}
}
