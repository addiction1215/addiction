package com.addiction.alertHistory.controller.alertHistory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.addiction.alertHistory.service.alertHistory.AlertHistoryReadService;
import com.addiction.alertHistory.service.alertHistory.AlertHistoryService;
import com.addiction.alertHistory.service.alertHistory.response.AlertHistoryResponse;
import com.addiction.alertHistory.service.alertHistory.response.AlertHistoryStatusResponse;
import com.addiction.global.ApiResponse;
import com.addiction.global.page.request.PageInfoRequest;
import com.addiction.global.page.response.PageCustom;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AlertHistoryController {

	private final AlertHistoryService alertHistoryService;
	private final AlertHistoryReadService alertHistoryReadService;

	@GetMapping("/api/v1/alertHistories")
	public ApiResponse<PageCustom<AlertHistoryResponse>> getAlertHistory(@ModelAttribute PageInfoRequest request) {
		return ApiResponse.ok(alertHistoryReadService.getAlertHistory(request.toServiceRequest()));
	}

	@PatchMapping("/api/v1/alertHistories/{id}")
	public ApiResponse<AlertHistoryStatusResponse> updateAlertHistoryStatus(@PathVariable Long id) {
		return ApiResponse.ok(alertHistoryService.updateAlertHistoryStatus(id));
	}
}
