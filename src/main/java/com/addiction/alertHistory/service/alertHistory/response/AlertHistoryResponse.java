package com.addiction.alertHistory.service.alertHistory.response;

import java.time.LocalDateTime;

import com.addiction.alertHistory.entity.AlertDestinationType;
import com.addiction.alertHistory.entity.AlertHistory;
import com.addiction.alertHistory.entity.AlertHistoryStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AlertHistoryResponse {

	private final Long id;
	private final String alertDescription;
	private final AlertHistoryStatus alertHistoryStatus;
	private final AlertDestinationType alertDestinationType;
	private final String alertDestinationInfo;
	private final LocalDateTime createdDate;

	@Builder
	private AlertHistoryResponse(Long id, String alertDescription, AlertHistoryStatus alertHistoryStatus,
		LocalDateTime createdDate, AlertDestinationType alertDestinationType, String alertDestinationInfo) {
		this.id = id;
		this.alertDescription = alertDescription;
		this.alertHistoryStatus = alertHistoryStatus;
		this.alertDestinationType = alertDestinationType;
		this.alertDestinationInfo = alertDestinationInfo;
		this.createdDate = createdDate;
	}

	public static AlertHistoryResponse of(AlertHistory alertHistory) {
		return AlertHistoryResponse.builder()
			.id(alertHistory.getId())
			.alertDescription(alertHistory.getAlertDescription())
			.alertHistoryStatus(alertHistory.getAlertHistoryStatus())
			.alertDestinationType(alertHistory.getAlertDestinationType())
			.alertDestinationInfo(alertHistory.getAlertDestinationInfo())
			.createdDate(alertHistory.getCreatedDate())
			.build();
	}
}
