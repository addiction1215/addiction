package com.addiction.alertHistory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.addiction.alertHistory.entity.AlertHistory;

public interface AlertHistoryRepository {

	void deleteByUserId(Long userId);

	List<AlertHistory> findByUserId(Long userId);

	Page<AlertHistory> findByUserId(Long userId, Pageable pageable);

	Optional<AlertHistory> findById(Long id);

	boolean hasUncheckedAlerts(Long userId);

	boolean hasFriendCode(Long userId, String friendCode);

	AlertHistory save(AlertHistory alertHistory);

	void deleteAllInBatch();

	void saveAll(List<AlertHistory> alertHistories);
}
