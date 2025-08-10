package com.addiction.alertHistory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.addiction.alertHistory.entity.AlertHistory;

public interface AlertHistoryJpaRepository extends JpaRepository<AlertHistory, Long> {

	void deleteByUserId(int userId);

	List<AlertHistory> findByUserId(int userId);
}
