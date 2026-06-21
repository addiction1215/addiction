package com.addiction.alertHistory.service.alertHistory;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.addiction.alertHistory.entity.AlertDestinationType;
import com.addiction.alertHistory.entity.AlertHistory;
import com.addiction.alertHistory.entity.AlertHistoryTabType;
import com.addiction.alertHistory.repository.AlertHistoryRepository;
import com.addiction.alertHistory.service.alertHistory.response.AlertHistoryResponse;
import com.addiction.global.exception.NotFoundException;
import com.addiction.global.page.request.PageInfoServiceRequest;
import com.addiction.global.page.response.PageCustom;
import com.addiction.global.page.response.PageableCustom;
import com.addiction.global.security.SecurityService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
@Slf4j
public class AlertHistoryReadService {

	private final AlertHistoryRepository alertHistoryRepository;
	private final SecurityService securityService;

	public AlertHistory findByOne(Long id) {
		return alertHistoryRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 알림 내역은 없습니다. id = " + id));
	}

	public PageCustom<AlertHistoryResponse> getAlertHistory(PageInfoServiceRequest request, AlertHistoryTabType tabType) {
		long userId = securityService.getCurrentLoginUserInfo().getUserId();

		Page<AlertHistory> alertHistoryPage = alertHistoryRepository.findByUserId(
			userId,
			tabType,
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

	public AlertHistoryResponse getNoticeAlertHistory(Long id) {
		log.info("[notice-detail] request received: alertHistoryId={}", id);

		long userId = securityService.getCurrentLoginUserInfo().getUserId();
		log.info("[notice-detail] lookup started: alertHistoryId={}, userId={}, destinationType={}",
			id, userId, AlertDestinationType.NOTICE);

		AlertHistory alertHistory = alertHistoryRepository.findByIdAndUserIdAndAlertDestinationType(
				id,
				userId,
				AlertDestinationType.NOTICE
			)
			.orElseThrow(() -> {
				log.warn("[notice-detail] lookup failed: alertHistoryId={}, userId={}, reason=not_found_or_not_notice",
					id, userId);
				return new NotFoundException("해당 공지사항은 없습니다. id = " + id);
			});

		log.info("[notice-detail] lookup succeeded: alertHistoryId={}, userId={}, destinationInfoPresent={}",
			alertHistory.getId(), userId, alertHistory.getAlertDestinationInfo() != null);

		return AlertHistoryResponse.of(alertHistory);
	}

	public boolean hasUncheckedAlerts() {
		long userId = securityService.getCurrentLoginUserInfo().getUserId();
		return alertHistoryRepository.hasUncheckedAlerts(userId);
	}

	public boolean hasFriendCode(Long userId, String friendCode) {
		return alertHistoryRepository.hasFriendCode(userId, friendCode);
	}

}
