package com.addiction.alertSetting.service;

import com.addiction.alertSetting.entity.AlertSetting;
import com.addiction.alertSetting.service.response.AlertSettingResponse;
import com.addiction.user.users.entity.User;

public interface AlertSettingReadService {

	AlertSettingResponse getAlertSetting();

	AlertSetting findByUserOrCreateDefault(User user);
}
