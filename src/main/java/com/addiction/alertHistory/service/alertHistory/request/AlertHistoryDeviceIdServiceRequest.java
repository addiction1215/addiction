package com.addiction.alertHistory.service.alertHistory.request;


import com.addiction.global.page.request.PageInfoServiceRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class AlertHistoryDeviceIdServiceRequest extends PageInfoServiceRequest {

	private String deviceId;

}

