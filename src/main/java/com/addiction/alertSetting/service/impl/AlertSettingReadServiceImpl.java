package com.addiction.alertSetting.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.addiction.alertSetting.entity.AlertSetting;
import com.addiction.alertSetting.repository.AlertSettingRepository;
import com.addiction.alertSetting.service.AlertSettingReadService;
import com.addiction.alertSetting.service.response.AlertSettingResponse;
import com.addiction.global.security.SecurityService;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.service.UserReadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlertSettingReadServiceImpl implements AlertSettingReadService {

	private final SecurityService securityService;
	private final UserReadService userReadService;
	private final AlertSettingRepository alertSettingRepository;

	@Override
	public AlertSettingResponse getAlertSetting() {
		User user = userReadService.findById(securityService.getCurrentLoginUserInfo().getUserId());
		AlertSetting alertSetting = findByUserOrCreateDefault(user);
		return AlertSettingResponse.createResponse(alertSetting);
	}

	@Override
	public AlertSetting findByUserOrCreateDefault(User user) {
		return alertSettingRepository.findByUser(user)
			.orElseGet(() -> alertSettingRepository.save(AlertSetting.createDefault(user)));
	}

}
