package com.addiction.user.users.service;

import com.addiction.IntegrationTestSupport;
import com.addiction.user.userCigarette.entity.UserCigarette;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;
import com.addiction.user.users.service.response.CumulativeChangeResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

class CumulativeChangeServiceTest extends IntegrationTestSupport {

    @Autowired
    private CumulativeChangeService cumulativeChangeService;

    @DisplayName("기존 혜택 계산과 같은 절약 금액 및 덜 피운 담배 개비 수를 조회한다.")
    @Test
    void 절약_금액과_덜_피운_담배_개비_수를_조회한다() {
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        user.updateSurvey("금연 화이팅", 10, 5000, 20, LocalDateTime.now().minusDays(3));
        userRepository.save(user);
        given(securityService.getCurrentLoginUserInfo()).willReturn(createLoginUserInfo(user.getId()));
        given(userCigaretteHistoryRepository.findMaxSmokePatienceTimeByUserId(user.getId())).willReturn(7200L);

        CumulativeChangeResponse response = cumulativeChangeService.findCumulativeChange();

        assertThat(response.getSavedMoney()).isEqualTo(15000L);
        assertThat(response.getReducedCigaretteCount()).isEqualTo(60L);
        assertThat(response.getLongestAbstinenceSeconds()).isEqualTo(7200L);
    }

    @DisplayName("오늘 기록과 과거 기록 중 더 긴 금연 시간을 반환한다.")
    @Test
    void 오늘과_과거_기록의_최장_금연_시간을_비교한다() {
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        user.updateSurvey("금연 화이팅", 10, 5000, 20, LocalDateTime.now().minusDays(3));
        userRepository.save(user);
        userCigaretteRepository.save(UserCigarette.createEntity(
                user, "테스트 주소", 14400L, LocalDateTime.now()));
        given(securityService.getCurrentLoginUserInfo()).willReturn(createLoginUserInfo(user.getId()));
        given(userCigaretteHistoryRepository.findMaxSmokePatienceTimeByUserId(user.getId())).willReturn(7200L);

        CumulativeChangeResponse response = cumulativeChangeService.findCumulativeChange();

        assertThat(response.getSavedMoney()).isZero();
        assertThat(response.getReducedCigaretteCount()).isZero();
        assertThat(response.getLongestAbstinenceSeconds()).isEqualTo(14400L);
    }

    @DisplayName("흡연 이력이 없으면 최장 금연 시간은 제공하지 않는다.")
    @Test
    void 흡연_이력이_없으면_최장_금연_시간은_null이다() {
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        user.updateSurvey("금연 화이팅", 10, 5000, 20, LocalDateTime.now());
        userRepository.save(user);
        given(securityService.getCurrentLoginUserInfo()).willReturn(createLoginUserInfo(user.getId()));
        given(userCigaretteHistoryRepository.findMaxSmokePatienceTimeByUserId(user.getId())).willReturn(null);

        CumulativeChangeResponse response = cumulativeChangeService.findCumulativeChange();

        assertThat(response.getLongestAbstinenceSeconds()).isNull();
    }
}
