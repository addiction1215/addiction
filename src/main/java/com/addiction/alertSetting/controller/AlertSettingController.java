package com.addiction.alertSetting.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.addiction.alertSetting.controller.request.AlertSettingUpdateRequest;
import com.addiction.alertSetting.service.AlertSettingReadService;
import com.addiction.alertSetting.service.AlertSettingService;
import com.addiction.alertSetting.service.response.AlertSettingResponse;
import com.addiction.global.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alert-setting")
public class AlertSettingController {

	private final AlertSettingService alertSettingService;
	private final AlertSettingReadService alertSettingReadService;

	@GetMapping
	public ApiResponse<AlertSettingResponse> getAlertSetting() {
		return ApiResponse.ok(alertSettingReadService.getAlertSetting());
	}

	@PatchMapping
	public ApiResponse<AlertSettingResponse> updateAlertSetting(
		@RequestBody @Valid AlertSettingUpdateRequest request) {
		return ApiResponse.ok(alertSettingService.updateAlertSetting(request.toServiceRequest()));
	}
}
