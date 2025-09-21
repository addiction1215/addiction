package com.addiction.alertHistory.service.alertHistory.response;

import com.addiction.alertHistory.entity.AlertHistoryStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AlertHistoryStatusResponse {
    private final AlertHistoryStatus alertHistoryStatus;

    @Builder
    private AlertHistoryStatusResponse(AlertHistoryStatus alertHistoryStatus) {
        this.alertHistoryStatus = alertHistoryStatus;
    }

    public static AlertHistoryStatusResponse of(AlertHistoryStatus alertHistoryStatus) {
        return AlertHistoryStatusResponse.builder()
            .alertHistoryStatus(alertHistoryStatus)
            .build();
    }
}
