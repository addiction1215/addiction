package com.addiction.alertHistory.service.alertHistory;

import com.addiction.alertHistory.entity.AlertHistory;
import com.addiction.alertHistory.entity.AlertHistoryStatus;
import com.addiction.alertHistory.repository.AlertHistoryRepository;
import com.addiction.alertHistory.service.alertHistory.request.AlertHistoryServiceRequest;
import com.addiction.alertHistory.service.alertHistory.response.AlertHistoryStatusResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AlertHistoryService {

    private final AlertHistoryRepository alertHistoryRepository;
    private final AlertHistoryReadService alertHistoryReadService;

    public AlertHistory createAlertHistory(AlertHistoryServiceRequest alertHistoryServiceRequest) {
        return alertHistoryRepository.save(alertHistoryServiceRequest.toEntity());
    }

    public AlertHistoryStatusResponse updateAlertHistoryStatus(Long id) {
        AlertHistory alertHistory = alertHistoryReadService.findByOne(id);
        alertHistory.updateAlertHistoryStatus(AlertHistoryStatus.CHECKED);

        return AlertHistoryStatusResponse.of(alertHistory.getAlertHistoryStatus());
    }
}
