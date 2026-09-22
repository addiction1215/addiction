package com.addiction.craving.service;

import com.addiction.IntegrationTestSupport;
import com.addiction.craving.entity.CravingSession;
import com.addiction.craving.entity.CravingSessionStatus;
import com.addiction.craving.repository.CravingSessionRepository;
import com.addiction.craving.service.response.CravingSessionCompleteResponse;
import com.addiction.global.exception.AddictionException;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

class CravingSessionServiceTest extends IntegrationTestSupport {
    @Autowired
    private CravingSessionService cravingSessionService;

    @Autowired
    private CravingSessionRepository cravingSessionRepository;

    @DisplayName("30초가 지난 갈망 대응을 완료하면 오늘 완료 횟수를 반환한다")
    @Test
    void completeSession() {
        User user = userRepository.save(createUser("craving@test.com", "password", SnsType.NORMAL, SettingStatus.COMPLETE));
        given(securityService.getCurrentLoginUserInfo()).willReturn(createLoginUserInfo(user.getId()));
        CravingSession session = cravingSessionRepository.save(
                CravingSession.start(user, LocalDateTime.now().minusSeconds(31))
        );

        CravingSessionCompleteResponse response = cravingSessionService.complete(session.getId());

        assertThat(response.getId()).isEqualTo(session.getId());
        assertThat(response.getTodayCompletedCount()).isEqualTo(1);
        assertThat(cravingSessionRepository.findById(session.getId()).orElseThrow().getStatus())
                .isEqualTo(CravingSessionStatus.COMPLETED);
    }

    @DisplayName("30초 전에 완료하면 완료 처리하지 않는다")
    @Test
    void rejectEarlyCompletion() {
        User user = userRepository.save(createUser("craving-early@test.com", "password", SnsType.NORMAL, SettingStatus.COMPLETE));
        given(securityService.getCurrentLoginUserInfo()).willReturn(createLoginUserInfo(user.getId()));
        CravingSession session = cravingSessionRepository.save(
                CravingSession.start(user, LocalDateTime.now().minusSeconds(29))
        );

        assertThatThrownBy(() -> cravingSessionService.complete(session.getId()))
                .isInstanceOf(AddictionException.class)
                .hasMessage("30초를 버틴 뒤 완료할 수 있습니다.");
    }
}
