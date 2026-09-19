package com.addiction.user.users.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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

public class UserReadServiceTest extends IntegrationTestSupport {

	@Autowired
	private UserReadService userReadService;

	@Autowired
	private UserRepository userRepository;

    @Autowired
    private UserSurveyResponseJpaRepository userSurveyResponseJpaRepository;

	@DisplayName("유저의 목표를 조회한다.")
	@Test
	void 유저의_목표를_조회한다() {
		//given
		User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
		userRepository.save(user);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		//when
		//then
		assertThat(userReadService.findPurpose().getPurpose()).isEqualTo("테스트 목표");
	}

	@DisplayName("유저의 간단한 프로필 정보를 조회한다.")
	@Test
	void 유저의_간단한_프로필_정보를_조회한다() {
		//given
		User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
		user.updateProfileUrl("test.com");
		userRepository.save(user);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		//when
		UserSimpleProfileResponse response = userReadService.findSimpleProfile();

		//then
		assertThat(response.getEmail()).isEqualTo("test@test.com");
		assertThat(response.getNickName()).isEqualTo("테스트 닉네임");
		assertThat(response.getSnsType()).isEqualTo(SnsType.KAKAO);
	}

    @DisplayName("설문 이력이 없으면 흡연 성향 비교를 제공하지 않는다.")
    @Test
    void 설문_이력이_없으면_흡연_성향_비교를_제공하지_않는다() {
        // given
        User user = userRepository.save(createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE));
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createLoginUserInfo(user.getId()));

        // when
        UserSmokingTendencyResponse response = userReadService.findSmokingTendency();

        // then
        assertThat(response.isHasSurvey()).isFalse();
        assertThat(response.isHasComparison()).isFalse();
        assertThat(response.getComparisonStatus()).isEqualTo(SmokingTendencyComparisonStatus.NOT_AVAILABLE);
    }

    @DisplayName("설문 이력이 한 번이면 흡연 성향은 제공하고 비교는 제공하지 않는다.")
    @Test
    void 설문_이력이_한_번이면_비교를_제공하지_않는다() {
        // given
        User user = userRepository.save(createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.COMPLETE));
        userSurveyResponseJpaRepository.save(UserSurveyResponse.create(user, 53));
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createLoginUserInfo(user.getId()));

        // when
        UserSmokingTendencyResponse response = userReadService.findSmokingTendency();

        // then
        assertThat(response.isHasSurvey()).isTrue();
        assertThat(response.isHasComparison()).isFalse();
        assertThat(response.getQuitMateScore()).isEqualTo(59);
        assertThat(response.getLevel()).isEqualTo(SmokingTendencyLevel.MODERATE);
        assertThat(response.getComparisonStatus()).isEqualTo(SmokingTendencyComparisonStatus.NOT_AVAILABLE);
    }

    @DisplayName("최근 설문 점수와 직전 설문 점수로 흡연 성향 변화를 조회한다.")
    @Test
    void 최근_설문_점수와_직전_설문_점수로_흡연_성향_변화를_조회한다() {
        // given
        User user = userRepository.save(createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.COMPLETE));
        userSurveyResponseJpaRepository.save(UserSurveyResponse.create(user, 60));
        userSurveyResponseJpaRepository.save(UserSurveyResponse.create(user, 53));
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createLoginUserInfo(user.getId()));

        // when
        UserSmokingTendencyResponse response = userReadService.findSmokingTendency();

        // then
        assertThat(response.isHasSurvey()).isTrue();
        assertThat(response.isHasComparison()).isTrue();
        assertThat(response.getRawScore()).isEqualTo(53);
        assertThat(response.getQuitMateScore()).isEqualTo(59);
        assertThat(response.getLevel()).isEqualTo(SmokingTendencyLevel.MODERATE);
        assertThat(response.getPreviousQuitMateScore()).isEqualTo(50);
        assertThat(response.getScoreChange()).isEqualTo(9);
        assertThat(response.getLevelChange()).isZero();
        assertThat(response.getComparisonStatus()).isEqualTo(SmokingTendencyComparisonStatus.SCORE_INCREASED);
    }

    @DisplayName("단계가 올라가면 점수 변화와 관계없이 단계 개선으로 판단한다.")
    @Test
    void 단계가_올라가면_단계_개선으로_판단한다() {
        // given
        User user = userRepository.save(createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.COMPLETE));
        userSurveyResponseJpaRepository.save(UserSurveyResponse.create(user, 99));
        userSurveyResponseJpaRepository.save(UserSurveyResponse.create(user, 21));
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createLoginUserInfo(user.getId()));

        // when
        UserSmokingTendencyResponse response = userReadService.findSmokingTendency();

        // then
        assertThat(response.getQuitMateScore()).isEqualTo(100);
        assertThat(response.getLevel()).isEqualTo(SmokingTendencyLevel.MILD);
        assertThat(response.getPreviousQuitMateScore()).isZero();
        assertThat(response.getLevelChange()).isEqualTo(2);
        assertThat(response.getComparisonStatus()).isEqualTo(SmokingTendencyComparisonStatus.LEVEL_IMPROVED);
    }

    @DisplayName("같은 단계에서 점수가 같으면 동일 결과로 판단한다.")
    @Test
    void 같은_단계에서_점수가_같으면_동일_결과로_판단한다() {
        // given
        User user = userRepository.save(createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.COMPLETE));
        userSurveyResponseJpaRepository.save(UserSurveyResponse.create(user, 53));
        userSurveyResponseJpaRepository.save(UserSurveyResponse.create(user, 53));
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createLoginUserInfo(user.getId()));

        // when
        UserSmokingTendencyResponse response = userReadService.findSmokingTendency();

        // then
        assertThat(response.getScoreChange()).isZero();
        assertThat(response.getLevelChange()).isZero();
        assertThat(response.getComparisonStatus()).isEqualTo(SmokingTendencyComparisonStatus.UNCHANGED);
    }

    @DisplayName("같은 단계에서 점수가 낮아지면 점수 하락으로 판단한다.")
    @Test
    void 같은_단계에서_점수가_낮아지면_점수_하락으로_판단한다() {
        // given
        User user = userRepository.save(createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.COMPLETE));
        userSurveyResponseJpaRepository.save(UserSurveyResponse.create(user, 53));
        userSurveyResponseJpaRepository.save(UserSurveyResponse.create(user, 60));
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createLoginUserInfo(user.getId()));

        // when
        UserSmokingTendencyResponse response = userReadService.findSmokingTendency();

        // then
        assertThat(response.getScoreChange()).isEqualTo(-9);
        assertThat(response.getLevelChange()).isZero();
        assertThat(response.getComparisonStatus()).isEqualTo(SmokingTendencyComparisonStatus.SCORE_DECREASED);
    }

    @DisplayName("단계가 내려가면 점수 변화와 관계없이 단계 하락으로 판단한다.")
    @Test
    void 단계가_내려가면_단계_하락으로_판단한다() {
        // given
        User user = userRepository.save(createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.COMPLETE));
        userSurveyResponseJpaRepository.save(UserSurveyResponse.create(user, 21));
        userSurveyResponseJpaRepository.save(UserSurveyResponse.create(user, 99));
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createLoginUserInfo(user.getId()));

        // when
        UserSmokingTendencyResponse response = userReadService.findSmokingTendency();

        // then
        assertThat(response.getQuitMateScore()).isZero();
        assertThat(response.getLevel()).isEqualTo(SmokingTendencyLevel.SEVERE);
        assertThat(response.getLevelChange()).isEqualTo(-2);
        assertThat(response.getComparisonStatus()).isEqualTo(SmokingTendencyComparisonStatus.LEVEL_WORSENED);
    }
}
