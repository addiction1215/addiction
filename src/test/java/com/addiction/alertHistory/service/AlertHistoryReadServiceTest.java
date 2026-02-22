package com.addiction.alertHistory.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.addiction.IntegrationTestSupport;
import com.addiction.alertHistory.entity.AlertDestinationType;
import com.addiction.alertHistory.entity.AlertHistory;
import com.addiction.alertHistory.entity.AlertHistoryStatus;
import com.addiction.alertHistory.entity.AlertHistoryTabType;
import com.addiction.alertHistory.service.alertHistory.AlertHistoryReadService;
import com.addiction.alertHistory.service.alertHistory.response.AlertHistoryResponse;
import com.addiction.global.page.request.PageInfoServiceRequest;
import com.addiction.global.page.response.PageCustom;
import com.addiction.user.push.entity.Push;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;

class AlertHistoryReadServiceTest extends IntegrationTestSupport {

	@Autowired
	private AlertHistoryReadService alertHistoryReadService;

	@DisplayName("알림 내역 ID를 받아서 알림 내역을 가져온다.")
	@Test
	void findByOne() {
		// given
		User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.INCOMPLETE);
		userRepository.save(user);

		Push push = createPush(user);
		pushRepository.save(push);

		AlertHistory alertHistory = createAlertHistory(user, "test1", AlertHistoryStatus.CHECKED);
		alertHistoryRepository.save(alertHistory);

		// when
		AlertHistory result = alertHistoryReadService.findByOne(alertHistory.getId());

		// then
		assertThat(result).extracting("id", "alertDescription", "alertHistoryStatus")
			.contains(alertHistory.getId(), alertHistory.getAlertDescription(), alertHistory.getAlertHistoryStatus());
	}

	@DisplayName("알림 내역 ID를 받아서 알림 내역이 있는지 검색했을 때 ID가 없는 예외상황이 발생한다.")
	@Test
	void findByOneWithException() {
		// given
		Long id = 0L;

		// when // then
		assertThatThrownBy(() -> alertHistoryReadService.findByOne(id))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("해당 알림 내역은 없습니다. id = " + id);
	}

	@DisplayName("ACTIVE 탭 알림 내역을 가져온다.")
	@Test
	void getAlertHistory_ACTIVE() {
		// given
		User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.INCOMPLETE);
		userRepository.save(user);

		Push push = createPush(user);
		pushRepository.save(push);

		AlertHistory alertHistory = createAlertHistory(user, "test1", AlertHistoryStatus.CHECKED);
		AlertHistory savedAlertHistory = alertHistoryRepository.save(alertHistory);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		PageInfoServiceRequest request = PageInfoServiceRequest.builder()
			.page(1)
			.size(10)
			.build();

		AlertHistoryResponse response = AlertHistoryResponse.of(savedAlertHistory);

		// when
		PageCustom<AlertHistoryResponse> result = alertHistoryReadService.getAlertHistory(request, AlertHistoryTabType.ACTIVE);

		// then
		assertThat(result.getContent()).extracting("id", "alertDescription", "alertHistoryStatus")
			.contains(
				tuple(response.getId(), response.getAlertDescription(), response.getAlertHistoryStatus())
			);

		assertThat(result.getPageInfo()).extracting("currentPage", "totalPage", "totalElement")
			.contains(1, 1, 1);
	}

	@DisplayName("NOTICE 탭은 alertDestinationType이 NOTICE인 것만 가져온다.")
	@Test
	void getAlertHistory_NOTICE() {
		// given
		User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.INCOMPLETE);
		userRepository.save(user);

		Push push = createPush(user);
		pushRepository.save(push);

		AlertHistory noticeAlert = AlertHistory.builder()
				.user(user)
				.alertDescription("공지사항")
				.alertHistoryStatus(AlertHistoryStatus.UNCHECKED)
				.alertDestinationType(AlertDestinationType.NOTICE)
				.build();
		AlertHistory activeAlert = createAlertHistory(user, "활동 알림", AlertHistoryStatus.UNCHECKED);
		alertHistoryRepository.saveAll(List.of(noticeAlert, activeAlert));

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		PageInfoServiceRequest request = PageInfoServiceRequest.builder()
			.page(1)
			.size(10)
			.build();

		// when
		PageCustom<AlertHistoryResponse> result = alertHistoryReadService.getAlertHistory(request, AlertHistoryTabType.NOTICE);

		// then
		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getContent().get(0).getAlertDescription()).isEqualTo("공지사항");
	}

	@DisplayName("유저가 읽지 않은 알림이 하나 이상인지 확인한다.")
	@Test
	void hasUncheckedAlerts() {
		// given
		User user1 = createUser("test1@test.com", "1234", SnsType.NORMAL, SettingStatus.INCOMPLETE);
		User user2 = createUser("test2@test.com", "1234", SnsType.NORMAL, SettingStatus.INCOMPLETE);
		userRepository.saveAll(List.of(user1, user2));

		Push push1 = createPush(user1);
		Push push2 = createPush(user2);
		pushRepository.saveAll(List.of(push1, push2));

		AlertHistory alertHistory1 = createAlertHistory(user1, "test1", AlertHistoryStatus.CHECKED);
		AlertHistory alertHistory2 = createAlertHistory(user1, "test2", AlertHistoryStatus.CHECKED);
		AlertHistory alertHistory3 = createAlertHistory(user1, "test3", AlertHistoryStatus.CHECKED);
		AlertHistory alertHistory4 = createAlertHistory(user2, "test4", AlertHistoryStatus.UNCHECKED);
		alertHistoryRepository.saveAll(List.of(alertHistory1, alertHistory2, alertHistory3, alertHistory4));

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user1.getId()));

		// when
		boolean result = alertHistoryReadService.hasUncheckedAlerts();

		// then
		assertThat(result).isFalse();
	}

	@DisplayName("유저가 이미 요청받은 초대인지 확인한다.")
	@Test
	void hasFriendCodeAlert() {
		// given
		User user1 = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.INCOMPLETE);
		userRepository.save(user1);

		Push push1 = createPush(user1);
		pushRepository.save(push1);

		AlertHistory alertHistory1 = createFriendCodeAlertHistory(user1, "test", AlertHistoryStatus.CHECKED);
		alertHistoryRepository.save(alertHistory1);

		// when
		boolean result = alertHistoryReadService.hasFriendCode(user1.getId(), "test");

		// then
		assertThat(result).isTrue();
	}
}
