package com.addiction.alertSetting.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.addiction.alertSetting.entity.AlertSetting;
import com.addiction.alertSetting.service.AlertSettingReadService;
import com.addiction.alertSetting.service.AlertSettingService;
import com.addiction.alertSetting.service.request.AlertSettingUpdateServiceRequest;
import com.addiction.alertSetting.service.response.AlertSettingResponse;
import com.addiction.global.security.SecurityService;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.service.UserReadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AlertSettingServiceImpl implements AlertSettingService {

	private final SecurityService securityService;
	private final UserReadService userReadService;
	private final AlertSettingReadService alertSettingReadService;

	@Override
	public AlertSettingResponse updateAlertSetting(AlertSettingUpdateServiceRequest request) {
		User user = userReadService.findById(securityService.getCurrentLoginUserInfo().getUserId());
		AlertSetting alertSetting = alertSettingReadService.findByUserOrCreateDefault(user);

		alertSetting.updateAlertSettings(
			request.getAll(),
			request.getSmokingWarning(),
			request.getLeaderboardRank(),
			request.getChallenge(),
			request.getReport()
		);

		return AlertSettingResponse.createResponse(alertSetting);
	}
}
