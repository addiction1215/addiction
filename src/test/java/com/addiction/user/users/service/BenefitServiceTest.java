package com.addiction.user.users.service;

import com.addiction.IntegrationTestSupport;
import com.addiction.user.userCigarette.entity.UserCigarette;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;
import com.addiction.user.users.service.response.BenefitResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

    @DisplayName("하루 절약액은 (하루 흡연 개피수 × 담배 1갑 가격 / 20)으로 계산한다.")
    @Test
    void 하루_절약액_계산식을_검증한다() {
        // given - 하루 10개피, 1갑 4500원 → 하루 절약액 = 10 * 4500 / 20 = 2250원
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        user.updateSurvey("금연 화이팅", 10, 4500, 10, LocalDateTime.now().minusDays(10));
        userRepository.save(user);

        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createLoginUserInfo(user.getId()));

        // when
        BenefitResponse response = benefitService.findMyBenefit();

        // then
        assertThat(response.getDailySavedMoney()).isEqualTo(2250L);
        assertThat(response.getNonSmokingDays()).isEqualTo(10L);
        assertThat(response.getSavedMoney()).isEqualTo(22500L);
    }
}
