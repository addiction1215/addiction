package com.addiction.alertHistory.repository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.addiction.alertHistory.entity.AlertHistory;
import com.addiction.alertHistory.entity.AlertHistoryTabType;
import com.addiction.alertHistory.repository.AlertHistoryJpaRepository;
import com.addiction.alertHistory.repository.AlertHistoryQueryRepository;
import com.addiction.alertHistory.repository.AlertHistoryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AlertHistoryRepositoryImpl implements AlertHistoryRepository {

	private final AlertHistoryQueryRepository alertHistoryQueryRepository;
	private final AlertHistoryJpaRepository alertHistoryJpaRepository;

	@Override
	public void deleteByUserId(Long userId) {
		alertHistoryJpaRepository.deleteByUserId(userId);
	}

	@Override
	public List<AlertHistory> findByUserId(Long userId) {
		return alertHistoryJpaRepository.findByUserId(userId);
	}

	@Override
	public Page<AlertHistory> findByUserId(Long userId, AlertHistoryTabType tabType, Pageable pageable) {
		return alertHistoryQueryRepository.findByUserId(userId, tabType, pageable);
	}

	@Override
	public Optional<AlertHistory> findById(Long id) {
		return alertHistoryJpaRepository.findById(id);
	}

	@Override
	public boolean hasUncheckedAlerts(Long userId) {
		return alertHistoryQueryRepository.hasUncheckedAlerts(userId);
	}

	@Override
	public boolean hasFriendCode(Long userId, String friendCode) {
		return alertHistoryQueryRepository.hasFriendCode(userId, friendCode);
	}

	@Override
	public AlertHistory save(AlertHistory alertHistory) {
		return alertHistoryJpaRepository.save(alertHistory);
	}

	@Override
	public void deleteAllInBatch() {
		alertHistoryJpaRepository.deleteAllInBatch();
	}

	@Override
	public void saveAll(List<AlertHistory> alertHistories) {
		alertHistoryJpaRepository.saveAll(alertHistories);
	}
}
