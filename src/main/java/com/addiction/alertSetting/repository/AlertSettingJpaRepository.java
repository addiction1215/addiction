package com.addiction.alertSetting.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.addiction.alertSetting.entity.AlertSetting;
import com.addiction.user.users.entity.User;

public interface AlertSettingJpaRepository extends JpaRepository<AlertSetting, Long> {
	Optional<AlertSetting> findByUser(User user);
}
