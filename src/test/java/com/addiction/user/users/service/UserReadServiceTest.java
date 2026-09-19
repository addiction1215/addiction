package com.addiction.user.users.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.addiction.IntegrationTestSupport;
import com.addiction.survey.userSurveyResponse.entity.UserSurveyResponse;
import com.addiction.survey.userSurveyResponse.repository.UserSurveyResponseJpaRepository;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;
import com.addiction.user.users.repository.UserRepository;
import com.addiction.user.users.service.response.SmokingTendencyComparisonStatus;
import com.addiction.user.users.service.response.SmokingTendencyLevel;
import com.addiction.user.users.service.response.UserSimpleProfileResponse;
import com.addiction.user.users.service.response.UserSmokingTendencyResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserReadServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserReadService userReadService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSurveyResponseJpaRepository userSurveyResponseJpaRepository;

    @DisplayName("유저의 목표를 조회한다.")
    @Test
    void 유저의_목표를_조회한다() {
        User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
        userRepository.save(user);
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createLoginUserInfo(user.getId()));

        assertThat(userReadService.findPurpose().getPurpose()).isEqualTo("테스트 목표");
    }

    @DisplayName("유저의 간단한 프로필 정보를 조회한다.")
    @Test
    void 유저의_간단한_프로필_정보를_조회한다() {
        User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
        user.updateProfileUrl("test.com");
        userRepository.save(user);
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createLoginUserInfo(user.getId()));

        UserSimpleProfileResponse response = userReadService.findSimpleProfile();

        assertThat(response.getEmail()).isEqualTo("test@test.com");
        assertThat(response.getNickName()).isEqualTo("테스트 닉네임");
        assertThat(response.getSnsType()).isEqualTo(SnsType.KAKAO);
    }

    @DisplayName("설문 이력이 없으면 흡연 성향을 제공하지 않는다.")
    @Test
    void 설문_이력이_없으면_흡연_성향을_제공하지_않는다() {
        User user = userRepository.save(createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE));
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createLoginUserInfo(user.getId()));

        UserSmokingTendencyResponse response = userReadService.findSmokingTendency();

        assertThat(response.isHasSurvey()).isFalse();
        assertThat(response.getComparisonStatus()).isEqualTo(SmokingTendencyComparisonStatus.NOT_AVAILABLE);
    }

    @DisplayName("최근 설문 결과로 흡연 성향을 조회한다.")
    @Test
    void 최근_설문_결과로_흡연_성향을_조회한다() {
        User user = userRepository.save(createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.COMPLETE));
        userSurveyResponseJpaRepository.save(UserSurveyResponse.create(user, 53));
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createLoginUserInfo(user.getId()));

        UserSmokingTendencyResponse response = userReadService.findSmokingTendency();

        assertThat(response.isHasSurvey()).isTrue();
        assertThat(response.getQuitMateScore()).isEqualTo(59);
        assertThat(response.getLevel()).isEqualTo(SmokingTendencyLevel.MODERATE);
        assertThat(response.getComparisonStatus()).isEqualTo(SmokingTendencyComparisonStatus.NOT_AVAILABLE);
    }

    @DisplayName("최근 설문과 직전 설문을 비교해 멘트 상태를 조회한다.")
    @Test
    void 최근_설문과_직전_설문을_비교해_멘트_상태를_조회한다() {
        User user = userRepository.save(createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.COMPLETE));
        userSurveyResponseJpaRepository.save(UserSurveyResponse.create(user, 60));
        userSurveyResponseJpaRepository.save(UserSurveyResponse.create(user, 53));
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createLoginUserInfo(user.getId()));

        UserSmokingTendencyResponse response = userReadService.findSmokingTendency();

        assertThat(response.getComparisonStatus()).isEqualTo(SmokingTendencyComparisonStatus.SCORE_INCREASED);
        assertThat(response.getScoreChange()).isEqualTo(9);
    }
}
