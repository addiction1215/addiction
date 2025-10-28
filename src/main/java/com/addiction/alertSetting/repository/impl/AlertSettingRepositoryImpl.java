package com.addiction.alertSetting.repository.impl;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.addiction.alertSetting.entity.AlertSetting;
import com.addiction.alertSetting.repository.AlertSettingJpaRepository;
import com.addiction.alertSetting.repository.AlertSettingRepository;
import com.addiction.user.users.entity.User;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AlertSettingRepositoryImpl implements AlertSettingRepository {

	private final AlertSettingJpaRepository alertSettingJpaRepository;

	@Override
	public AlertSetting save(AlertSetting alertSetting) {
		return alertSettingJpaRepository.save(alertSetting);
	}

	@Override
	public Optional<AlertSetting> findByUser(User user) {
		return alertSettingJpaRepository.findByUser(user);
	}

	@Override
	public void deleteAllInBatch() {
		alertSettingJpaRepository.deleteAllInBatch();
	}
}
