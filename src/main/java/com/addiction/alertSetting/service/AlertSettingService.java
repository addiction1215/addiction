package com.addiction.alertSetting.service;

import com.addiction.alertSetting.service.request.AlertSettingUpdateServiceRequest;
import com.addiction.alertSetting.service.response.AlertSettingResponse;

public interface AlertSettingService {

	AlertSettingResponse updateAlertSetting(AlertSettingUpdateServiceRequest request);
}
