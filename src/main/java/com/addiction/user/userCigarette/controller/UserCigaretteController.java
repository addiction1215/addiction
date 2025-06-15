package com.addiction.user.userCigarette.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.addiction.global.ApiResponse;
import com.addiction.user.userCigarette.service.UserCigaretteReadService;
import com.addiction.user.userCigarette.service.UserCigaretteService;
import com.addiction.user.userCigarette.service.request.ChangeType;
import com.addiction.user.userCigarette.service.response.UserCigaretteFindResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/cigarette")
public class UserCigaretteController {

	private final UserCigaretteService userCigaretteService;
	private final UserCigaretteReadService userCigaretteReadService;

	@PatchMapping("/{changeType}")
	public ApiResponse<UserCigaretteFindResponse> changeCigaretteCount(
		@PathVariable("changeType") ChangeType changeType) {
		return ApiResponse.ok(userCigaretteService.changeCigaretteCount(changeType));
	}

	@GetMapping
	public ApiResponse<UserCigaretteFindResponse> findCount() {
		return ApiResponse.ok(userCigaretteReadService.findUserCigaretteCount());
	}

}
