package com.addiction.alertSetting.service;

import com.addiction.IntegrationTestSupport;
import com.addiction.alertSetting.entity.AlertSetting;
import com.addiction.alertSetting.entity.enums.AlertType;
import com.addiction.alertSetting.service.request.AlertSettingUpdateServiceRequest;
import com.addiction.alertSetting.service.response.AlertSettingResponse;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class AlertSettingServiceTest extends IntegrationTestSupport {

    @Autowired
    private AlertSettingService alertSettingService;

    @Autowired
    private AlertSettingReadService alertSettingReadService;

    @DisplayName("알림 설정을 조회한다.")
    @Test
    void 알림_설정을_조회한다() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.COMPLETE);
        User savedUser = userRepository.save(user);

        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createLoginUserInfo(savedUser.getId()));

        // when
        AlertSettingResponse response = alertSettingReadService.getAlertSetting();

        // then
        assertThat(response)
                .extracting("all", "smokingWarning", "leaderboardRank", "challenge", "report")
                .contains(AlertType.ON, AlertType.ON, AlertType.ON, AlertType.ON, AlertType.ON);
    }

    @DisplayName("알림 설정이 없으면 기본값으로 생성하여 조회한다.")
    @Test
    void 알림_설정이_없으면_기본값으로_생성하여_조회한다() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.COMPLETE);
        User savedUser = userRepository.save(user);

        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createLoginUserInfo(savedUser.getId()));

        // when
        AlertSettingResponse response = alertSettingReadService.getAlertSetting();

        // then
        assertThat(response)
                .extracting("all", "smokingWarning", "leaderboardRank", "challenge", "report")
                .contains(AlertType.ON, AlertType.ON, AlertType.ON, AlertType.ON, AlertType.ON);

        // 데이터베이스에 저장되었는지 확인
        AlertSetting alertSetting = alertSettingRepository.findByUser(savedUser).get();
        assertThat(alertSetting).isNotNull();
    }

    @DisplayName("알림 설정을 수정한다.")
    @Test
    void 알림_설정을_수정한다() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.COMPLETE);
        User savedUser = userRepository.save(user);

        AlertSetting alertSetting = createAlertSetting(savedUser, AlertType.ON, AlertType.ON,
                AlertType.ON, AlertType.ON, AlertType.ON);
        alertSettingRepository.save(alertSetting);

        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createLoginUserInfo(savedUser.getId()));

        AlertSettingUpdateServiceRequest request = AlertSettingUpdateServiceRequest.builder()
                .all(AlertType.OFF)
                .smokingWarning(AlertType.OFF)
                .leaderboardRank(AlertType.ON)
                .challenge(AlertType.OFF)
                .report(AlertType.ON)
                .build();

        // when
        AlertSettingResponse response = alertSettingService.updateAlertSetting(request);

        // then
        assertThat(response)
                .extracting("all", "smokingWarning", "leaderboardRank", "challenge", "report")
                .contains(AlertType.OFF, AlertType.OFF, AlertType.ON, AlertType.OFF, AlertType.ON);
    }

    @DisplayName("알림 설정이 없을 때 수정 요청시 기본값으로 생성 후 수정한다.")
    @Test
    void 알림_설정이_없을_때_수정_요청시_기본값으로_생성_후_수정한다() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.COMPLETE);
        User savedUser = userRepository.save(user);

        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createLoginUserInfo(savedUser.getId()));

        AlertSettingUpdateServiceRequest request = AlertSettingUpdateServiceRequest.builder()
                .all(AlertType.OFF)
                .smokingWarning(AlertType.ON)
                .leaderboardRank(AlertType.OFF)
                .challenge(AlertType.ON)
                .report(AlertType.OFF)
                .build();

        // when
        AlertSettingResponse response = alertSettingService.updateAlertSetting(request);

        // then
        assertThat(response)
                .extracting("all", "smokingWarning", "leaderboardRank", "challenge", "report")
                .contains(AlertType.OFF, AlertType.ON, AlertType.OFF, AlertType.ON, AlertType.OFF);

        // 데이터베이스에 저장되었는지 확인
        AlertSetting alertSetting = alertSettingRepository.findByUser(savedUser).get();
        assertThat(alertSetting).isNotNull();
    }

    @DisplayName("여러 번 알림 설정을 수정해도 정상적으로 동작한다.")
    @Test
    void 여러_번_알림_설정을_수정해도_정상적으로_동작한다() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.COMPLETE);
        User savedUser = userRepository.save(user);

        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createLoginUserInfo(savedUser.getId()));

        // 첫 번째 수정
        AlertSettingUpdateServiceRequest request1 = AlertSettingUpdateServiceRequest.builder()
                .all(AlertType.OFF)
                .smokingWarning(AlertType.OFF)
                .leaderboardRank(AlertType.OFF)
                .challenge(AlertType.OFF)
                .report(AlertType.OFF)
                .build();

        alertSettingService.updateAlertSetting(request1);

        // 두 번째 수정
        AlertSettingUpdateServiceRequest request2 = AlertSettingUpdateServiceRequest.builder()
                .all(AlertType.ON)
                .smokingWarning(AlertType.ON)
                .leaderboardRank(AlertType.ON)
                .challenge(AlertType.ON)
                .report(AlertType.ON)
                .build();

        // when
        AlertSettingResponse response = alertSettingService.updateAlertSetting(request2);

        // then
        assertThat(response)
                .extracting("all", "smokingWarning", "leaderboardRank", "challenge", "report")
                .contains(AlertType.ON, AlertType.ON, AlertType.ON, AlertType.ON, AlertType.ON);
    }
}
