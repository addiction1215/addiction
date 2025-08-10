package com.addiction.alertHistory.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.addiction.IntegrationTestSupport;
import com.addiction.alertHistory.entity.AlertDestinationType;
import com.addiction.alertHistory.entity.AlertHistory;
import com.addiction.alertHistory.entity.AlertHistoryStatus;
import com.addiction.alertHistory.service.alertHistory.AlertHistoryService;
import com.addiction.alertHistory.service.alertHistory.request.AlertHistoryServiceRequest;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;
import com.addiction.user.users.repository.UserRepository;

public class AlertHistoryServiceTest extends IntegrationTestSupport {

	@Autowired
	private AlertHistoryService alertHistoryService;

	@Autowired
	private UserRepository userRepository;

	@DisplayName("푸시알림 발송 이력을 저장한다.")
	@Test
	void save() {
		User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.INCOMPLETE);
		userRepository.save(user);

		AlertHistoryServiceRequest alertHistoryServiceRequest = AlertHistoryServiceRequest.builder()
			.user(user)
			.alertDescription("알림테스트 내용")
			.alertDestinationType(AlertDestinationType.FRIEND_CODE)
			.alertDestinationInfo("테스트")
			.build();

		AlertHistory alertHistory = alertHistoryService.createAlertHistory(alertHistoryServiceRequest);
		assertThat(alertHistory).extracting("user", "alertDescription", "alertHistoryStatus", "alertDestinationType",
				"alertDestinationInfo")
			.contains(user, "알림테스트 내용", AlertHistoryStatus.UNCHECKED, AlertDestinationType.FRIEND_CODE, "테스트");
	}

}
