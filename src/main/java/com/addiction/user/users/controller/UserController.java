package com.addiction.user.users.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.addiction.global.ApiResponse;
import com.addiction.user.users.controller.request.UserSaveRequest;
import com.addiction.user.users.controller.request.UserUpdateRequest;
import com.addiction.user.users.controller.request.UserUpdateSurveyRequest;
import com.addiction.user.users.service.UserReadService;
import com.addiction.user.users.service.response.UserSaveResponse;
import com.addiction.user.users.service.response.UserStartDateResponse;
import com.addiction.user.users.service.response.UserUpdateResponse;
import com.addiction.user.users.service.response.UserUpdateSurveyResponse;
import com.addiction.user.users.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

	private final UserService userService;
	private final UserReadService userReadService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse<UserSaveResponse> save(@RequestBody @Valid UserSaveRequest userSaveRequest) {
		return ApiResponse.created(userService.save(userSaveRequest.toServiceRequest()));
	}

	@PatchMapping
	public ApiResponse<UserUpdateResponse> update(@RequestBody @Valid UserUpdateRequest userUpdateRequest) {
		return ApiResponse.ok(userService.update(userUpdateRequest.toServiceRequest()));
	}

	@PatchMapping("/survey")
	public ApiResponse<UserUpdateSurveyResponse> updateSurvey(@RequestBody @Valid UserUpdateSurveyRequest userUpdateSurveyRequest) {
		return ApiResponse.ok(userService.updateSurvey(userUpdateSurveyRequest.toServiceRequest()));
	}

	@GetMapping("/startDate")
	public ApiResponse<UserStartDateResponse> findStartDate() {
		return ApiResponse.ok(userReadService.findStartDate());
	}
}
