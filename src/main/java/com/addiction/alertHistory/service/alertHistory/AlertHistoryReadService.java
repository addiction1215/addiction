package com.addiction.alertHistory.service.alertHistory;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.addiction.alertHistory.entity.AlertHistory;
import com.addiction.alertHistory.repository.AlertHistoryRepository;
import com.addiction.alertHistory.service.alertHistory.response.AlertHistoryResponse;
import com.addiction.global.page.request.PageInfoServiceRequest;
import com.addiction.global.page.response.PageCustom;
import com.addiction.global.page.response.PageableCustom;
import com.addiction.global.security.SecurityService;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AlertHistoryReadService {

	private final AlertHistoryRepository alertHistoryRepository;
	private final SecurityService securityService;

	public AlertHistory findByOne(Long id) {
		return alertHistoryRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 알림 내역은 없습니다. id = " + id));
	}

	public PageCustom<AlertHistoryResponse> getAlertHistory(PageInfoServiceRequest request) {
		int userId = securityService.getCurrentLoginUserInfo().getUserId();

		Page<AlertHistory> alertHistoryPage = alertHistoryRepository.findByUserId(
			userId,
			request.toPageable()
		);

		List<AlertHistoryResponse> alertHistoryResponseList = alertHistoryPage.getContent().stream()
			.map(AlertHistoryResponse::of)
			.toList();

		return PageCustom.<AlertHistoryResponse>builder()
			.content(alertHistoryResponseList)
			.pageInfo(PageableCustom.of(alertHistoryPage))
			.build();
	}

	public boolean hasUncheckedAlerts() {
		int userId = securityService.getCurrentLoginUserInfo().getUserId();
		return alertHistoryRepository.hasUncheckedAlerts(userId);
	}

	public boolean hasFriendCode(int userId, String friendCode) {
		return alertHistoryRepository.hasFriendCode(userId, friendCode);
	}

}
