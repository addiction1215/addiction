package com.addiction.alertHistory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.addiction.alertHistory.entity.AlertDestinationType;
import com.addiction.alertHistory.entity.AlertHistory;
import com.addiction.alertHistory.entity.AlertHistoryTabType;

public interface AlertHistoryRepository {

	void deleteByUserId(Long userId);

	List<AlertHistory> findByUserId(Long userId);

	Page<AlertHistory> findByUserId(Long userId, AlertHistoryTabType tabType, Pageable pageable);

	Optional<AlertHistory> findById(Long id);

	Optional<AlertHistory> findByIdAndUserIdAndAlertDestinationType(
		Long id,
		Long userId,
		AlertDestinationType alertDestinationType
	);

	boolean hasUncheckedAlerts(Long userId);

	boolean hasFriendCode(Long userId, String friendCode);

	AlertHistory save(AlertHistory alertHistory);

	void deleteAllInBatch();

	void saveAll(List<AlertHistory> alertHistories);
}
