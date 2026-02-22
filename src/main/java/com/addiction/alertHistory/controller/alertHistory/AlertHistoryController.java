package com.addiction.alertHistory.controller.alertHistory;

import org.springframework.web.bind.annotation.*;

import com.addiction.alertHistory.entity.AlertHistoryTabType;
import com.addiction.alertHistory.service.alertHistory.AlertHistoryReadService;
import com.addiction.alertHistory.service.alertHistory.AlertHistoryService;
import com.addiction.alertHistory.service.alertHistory.response.AlertHistoryResponse;
import com.addiction.alertHistory.service.alertHistory.response.AlertHistoryStatusResponse;
import com.addiction.global.ApiResponse;
import com.addiction.global.page.request.PageInfoRequest;
import com.addiction.global.page.response.PageCustom;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/alertHistories")
@RequiredArgsConstructor
public class AlertHistoryController {

	private final AlertHistoryService alertHistoryService;
	private final AlertHistoryReadService alertHistoryReadService;

	@GetMapping
	public ApiResponse<PageCustom<AlertHistoryResponse>> getAlertHistory(
			@ModelAttribute PageInfoRequest request,
			@RequestParam AlertHistoryTabType tabType) {
		return ApiResponse.ok(alertHistoryReadService.getAlertHistory(request.toServiceRequest(), tabType));
	}

	@PatchMapping("/{id}")
	public ApiResponse<AlertHistoryStatusResponse> updateAlertHistoryStatus(@PathVariable Long id) {
		return ApiResponse.ok(alertHistoryService.updateAlertHistoryStatus(id));
	}
}
