package com.addiction.user.users.controller;

import com.addiction.user.users.controller.request.UserUpdateProfileRequest;
import com.addiction.user.users.service.response.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.addiction.global.ApiResponse;
import com.addiction.user.users.controller.request.UserUpdatePurposeRequest;
import com.addiction.user.users.controller.request.UserUpdateRequest;
import com.addiction.user.users.controller.request.UserUpdateSurveyRequest;
import com.addiction.user.users.service.UserReadService;
import com.addiction.user.users.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

	private final UserService userService;
	private final UserReadService userReadService;

	@PatchMapping
	public ApiResponse<UserUpdateResponse> update(@RequestBody @Valid UserUpdateRequest userUpdateRequest) {
		return ApiResponse.ok(userService.update(userUpdateRequest.toServiceRequest()));
	}

	@PatchMapping("/survey")
	public ApiResponse<UserUpdateSurveyResponse> updateSurvey(
		@RequestBody @Valid UserUpdateSurveyRequest userUpdateSurveyRequest) {
		return ApiResponse.ok(userService.updateSurvey(userUpdateSurveyRequest.toServiceRequest()));
	}

	@GetMapping("/startDate")
	public ApiResponse<UserStartDateResponse> findStartDate() {
		return ApiResponse.ok(userReadService.findStartDate());
	}

	@GetMapping("/purpose")
	public ApiResponse<UserPurposeResponse> findPurpose() {
		return ApiResponse.ok(userReadService.findPurpose());
	}

	@PatchMapping("/purpose")
	public ApiResponse<UserUpdatePurposeResponse> updatePurpose(@RequestBody @Valid UserUpdatePurposeRequest userUpdatePurposeRequest) {
		return ApiResponse.ok(userService.updatePurpose(userUpdatePurposeRequest.toServiceRequest()));
	}

	@PatchMapping("/profile")
	public ApiResponse<UserUpdateProfileResponse> updateProfile(@RequestBody @Valid UserUpdateProfileRequest userUpdateProfileRequest) {
		return ApiResponse.ok(userService.updateProfile(userUpdateProfileRequest.toServiceRequest()));
	}

}
