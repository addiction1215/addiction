package com.addiction.user.users.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.addiction.global.ApiResponse;
import com.addiction.user.users.dto.controller.request.UserSaveRequest;
import com.addiction.user.users.dto.controller.request.UserUpdateRequest;
import com.addiction.user.users.dto.service.response.UserSaveResponse;
import com.addiction.user.users.dto.service.response.UserUpdateResponse;
import com.addiction.user.users.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

	private final UserService userService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse<UserSaveResponse> save(@RequestBody @Valid UserSaveRequest userSaveRequest) {
		return ApiResponse.created(userService.save(userSaveRequest.toServiceRequest()));
	}

	@PatchMapping
	public ApiResponse<UserUpdateResponse> update(@RequestBody @Valid UserUpdateRequest userUpdateRequest) {
		return ApiResponse.ok(userService.update(userUpdateRequest.toServiceRequest()));
	}
}
