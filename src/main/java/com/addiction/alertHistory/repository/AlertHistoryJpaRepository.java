package com.addiction.alertHistory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.addiction.alertHistory.entity.AlertDestinationType;
import com.addiction.alertHistory.entity.AlertHistory;

public interface AlertHistoryJpaRepository extends JpaRepository<AlertHistory, Long> {

	void deleteByUserId(Long userId);

	List<AlertHistory> findByUserId(Long userId);

	Optional<AlertHistory> findByIdAndUserIdAndAlertDestinationType(
		Long id,
		Long userId,
		AlertDestinationType alertDestinationType
	);
}
