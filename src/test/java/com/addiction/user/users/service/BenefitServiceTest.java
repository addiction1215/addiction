package com.addiction.user.users.service;

import com.addiction.IntegrationTestSupport;
import com.addiction.user.userCigarette.entity.UserCigarette;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;
import com.addiction.user.users.service.response.BenefitResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class BenefitServiceTest extends IntegrationTestSupport {

    @Autowired
    private BenefitService benefitService;

    @DisplayName("오늘 흡연 기록이 있으면 해당 날짜로부터 금연 일수를 계산한다.")
    @Test
    void 오늘_흡연_기록이_있으면_해당_날짜로부터_금연_일수를_계산한다() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        user.updateSurvey("금연 화이팅", 10, 5000, 20, LocalDateTime.now().minusDays(30));
        userRepository.save(user);

        UserCigarette cigarette = createUserCigarette(user);
        userCigaretteRepository.save(cigarette);

        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createLoginUserInfo(user.getId()));

        // when
        BenefitResponse response = benefitService.findMyBenefit();

        // then
        assertThat(response.getNonSmokingDays()).isEqualTo(0);
        assertThat(response.getDailySavedMoney()).isEqualTo(5000L);
        assertThat(response.getSavedMoney()).isEqualTo(0L);
    }

    @DisplayName("흡연 기록이 없으면 User의 startDate로부터 금연 일수를 계산한다.")
    @Test
    void 흡연_기록이_없으면_startDate로부터_금연_일수를_계산한다() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        user.updateSurvey("금연 화이팅", 10, 5000, 20, LocalDateTime.now().minusDays(30));
        userRepository.save(user);

        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createLoginUserInfo(user.getId()));

        // when
        BenefitResponse response = benefitService.findMyBenefit();

        // then
        assertThat(response.getNonSmokingDays()).isEqualTo(30L);
        assertThat(response.getDailySavedMoney()).isEqualTo(5000L);
        assertThat(response.getSavedMoney()).isEqualTo(150000L);
    }

    @DisplayName("하루 절약액은 흡연량과 무관하게 설문에서 입력한 하루 지출액이다.")
    @ParameterizedTest
    @ValueSource(ints = {10, 20, 40})
    void 하루_절약액은_설문의_하루_지출액이다(int cigaretteCount) {
        // given - 하루 지출액 2000원, 금연 8일 → 절약액 16000원
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        user.updateSurvey("금연 화이팅", 10, 2000, cigaretteCount, LocalDateTime.now().minusDays(8));
        userRepository.save(user);

        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createLoginUserInfo(user.getId()));

        // when
        BenefitResponse response = benefitService.findMyBenefit();

        // then
        assertThat(response.getDailySavedMoney()).isEqualTo(2000L);
        assertThat(response.getNonSmokingDays()).isEqualTo(8L);
        assertThat(response.getSavedMoney()).isEqualTo(16000L);
    }
}
