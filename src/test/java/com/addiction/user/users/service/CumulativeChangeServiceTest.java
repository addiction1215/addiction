package com.addiction.user.users.service;

import com.addiction.IntegrationTestSupport;
import com.addiction.smokefree.entity.SmokeFreeConfirmation;
import com.addiction.smokefree.repository.SmokeFreeConfirmationRepository;
import com.addiction.user.userCigarette.entity.UserCigarette;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;
import com.addiction.user.users.service.response.CumulativeChangeResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

class CumulativeChangeServiceTest extends IntegrationTestSupport {

    @Autowired
    private CumulativeChangeService cumulativeChangeService;

    @Autowired
    private SmokeFreeConfirmationRepository smokeFreeConfirmationRepository;

    @Autowired
    private Clock koreaClock;

    @DisplayName("가입일부터 확인된 금연일만 누적하고 흡연일은 제외한다.")
    @Test
    void 절약_금액과_덜_피운_담배_개비_수를_조회한다() {
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        user.updateSurvey("금연 화이팅", 10, 2000, 10, LocalDateTime.now().minusDays(3));
        userRepository.save(user);
        LocalDate today = LocalDate.now(koreaClock);
        ReflectionTestUtils.setField(user, "createdDate", today.minusDays(2).atStartOfDay());
        userRepository.save(user);
        smokeFreeConfirmationRepository.save(SmokeFreeConfirmation.confirm(user, today.minusDays(2)));
        smokeFreeConfirmationRepository.save(SmokeFreeConfirmation.confirm(user, today.minusDays(1)));
        smokeFreeConfirmationRepository.save(SmokeFreeConfirmation.confirm(user, today));
        given(userCigaretteHistoryRepository.findSmokedDatesByUserIdAndDateBetween(
                user.getId(), today.minusDays(2), today)).willReturn(Set.of(today.minusDays(1)));
        given(securityService.getCurrentLoginUserInfo()).willReturn(createLoginUserInfo(user.getId()));
        given(userCigaretteHistoryRepository.findMaxSmokePatienceTimeByUserId(user.getId())).willReturn(7200L);

        CumulativeChangeResponse response = cumulativeChangeService.findCumulativeChange();

        assertThat(response.getSavedMoney()).isEqualTo(4000L);
        assertThat(response.getReducedCigaretteCount()).isEqualTo(20L);
        assertThat(response.getNonSmokingDays()).isEqualTo(3L);
        assertThat(response.getTotalSmokeFreeDays()).isEqualTo(2L);
        assertThat(response.getLongestAbstinenceSeconds()).isEqualTo(7200L);
    }

    @DisplayName("오늘 기록과 과거 기록 중 더 긴 금연 시간을 반환한다.")
    @Test
    void 오늘과_과거_기록의_최장_금연_시간을_비교한다() {
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        user.updateSurvey("금연 화이팅", 10, 5000, 20, LocalDateTime.now().minusDays(3));
        userRepository.save(user);
        LocalDate today = LocalDate.now(koreaClock);
        ReflectionTestUtils.setField(user, "createdDate", today.minusDays(1).atStartOfDay());
        userRepository.save(user);
        smokeFreeConfirmationRepository.save(SmokeFreeConfirmation.confirm(user, today.minusDays(1)));
        userCigaretteRepository.save(UserCigarette.createEntity(
                user, "테스트 주소", 14400L, LocalDateTime.now()));
        given(securityService.getCurrentLoginUserInfo()).willReturn(createLoginUserInfo(user.getId()));
        given(userCigaretteHistoryRepository.findMaxSmokePatienceTimeByUserId(user.getId())).willReturn(7200L);

        CumulativeChangeResponse response = cumulativeChangeService.findCumulativeChange();

        assertThat(response.getSavedMoney()).isEqualTo(5000L);
        assertThat(response.getReducedCigaretteCount()).isEqualTo(20L);
        assertThat(response.getNonSmokingDays()).isZero();
        assertThat(response.getTotalSmokeFreeDays()).isEqualTo(1L);
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
        assertThat(response.getTotalSmokeFreeDays()).isZero();
    }

    @DisplayName("마지막 흡연 날짜를 금연 시작 날짜보다 우선하여 연속 금연 일수를 조회한다.")
    @Test
    void 마지막_흡연_날짜로_연속_금연_일수를_조회한다() {
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        user.updateSurvey("금연 화이팅", 10, 2000, 10, LocalDateTime.now().minusDays(30));
        user.updateLastSmoking(LocalDateTime.now().minusDays(8), "테스트 주소");
        userRepository.save(user);
        given(securityService.getCurrentLoginUserInfo()).willReturn(createLoginUserInfo(user.getId()));

        CumulativeChangeResponse response = cumulativeChangeService.findCumulativeChange();

        assertThat(response.getNonSmokingDays()).isEqualTo(8L);
        assertThat(response.getSavedMoney()).isZero();
        assertThat(response.getReducedCigaretteCount()).isZero();
        assertThat(response.getTotalSmokeFreeDays()).isZero();
    }
}
