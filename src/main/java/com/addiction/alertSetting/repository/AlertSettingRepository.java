package com.addiction.alertSetting.repository;

import java.util.Optional;

import com.addiction.alertSetting.entity.AlertSetting;
import com.addiction.user.users.entity.User;

public interface AlertSettingRepository {
	AlertSetting save(AlertSetting alertSetting);

	Optional<AlertSetting> findByUser(User user);

	void deleteAllInBatch();
}
