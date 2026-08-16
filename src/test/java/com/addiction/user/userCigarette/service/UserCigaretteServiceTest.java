package com.addiction.user.userCigarette.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.addiction.IntegrationTestSupport;
import com.addiction.user.userCigarette.service.request.ChangeType;
import com.addiction.user.userCigarette.service.request.UserCigaretteChangeServiceRequest;
import com.addiction.user.userCigaretteHistory.document.CigaretteHistoryDocument;
import com.addiction.user.userCigaretteHistory.service.UserCigaretteHistoryService;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;
import com.addiction.user.users.service.BenefitService;
import com.addiction.user.users.service.response.BenefitResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class UserCigaretteServiceTest extends IntegrationTestSupport {

	@Autowired
	private UserCigaretteService userCigaretteService;

	@Autowired
	private UserCigaretteHistoryService userCigaretteHistoryService;

	@Autowired
	private BenefitService benefitService;


	@DisplayName("유저의 흡연 갯수를 증가한다")
	@Test
	void 유저의_흡연_갯수를_증가한다() {
		// given
		User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
		userRepository.save(user);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		UserCigaretteChangeServiceRequest userCigaretteChangeServiceRequest = UserCigaretteChangeServiceRequest.builder()
			.changeType(ChangeType.ADD)
			.address("서울시 강남구 역삼동")
			.build();

		// when
		// then
		userCigaretteService.changeCigarette(userCigaretteChangeServiceRequest);

		assertThat(userCigaretteRepository.findAll())
			.hasSize(1)
			.extracting("address")
			.containsExactly("서울시 강남구 역삼동");
	}

	@DisplayName("유저의 흡연 갯수를 감소한다")
	@Test
	@Transactional
	void 유저의_흡연_갯수를_감소한다() {
		// given
		User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
		userRepository.save(user);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		UserCigaretteChangeServiceRequest userCigaretteChangeServiceRequest = UserCigaretteChangeServiceRequest.builder()
			.changeType(ChangeType.ADD)
			.address("서울시 강남구 역삼동")
			.build();

		UserCigaretteChangeServiceRequest userCigaretteChangeServiceMinusRequest = UserCigaretteChangeServiceRequest.builder()
			.changeType(ChangeType.MINUS)
			.build();

		// when
		// then
		userCigaretteService.changeCigarette(userCigaretteChangeServiceRequest);
		userCigaretteService.changeCigarette(userCigaretteChangeServiceMinusRequest);
		assertThat(userCigaretteRepository.findAll())
			.hasSize(0);
	}

	@DisplayName("ADD는 마지막 흡연 시각과 혜택을 오늘로 갱신하고, MINUS는 이전 이력과 혜택을 복구한다")
	@Test
	@Transactional
	void add_then_minus_restores_last_smoke_at_and_benefit() {
		// given
		User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.COMPLETE);
		user.updateSurvey("금연 화이팅", 10, 5000, 20, LocalDateTime.now().minusDays(10));
		userRepository.save(user);

		LocalDateTime previousSmokeTime = LocalDateTime.now().minusDays(3).withNano(0);
		CigaretteHistoryDocument previousHistory = CigaretteHistoryDocument.builder()
			.userId(user.getId())
			.history(List.of(CigaretteHistoryDocument.History.builder()
				.address("이전 주소")
				.smokeTime(previousSmokeTime)
				.build()))
			.build();
		given(userCigaretteHistoryRepository.findLatestByUserId(user.getId())).willReturn(previousHistory);
		given(securityService.getCurrentLoginUserInfo()).willReturn(createLoginUserInfo(user.getId()));

		UserCigaretteChangeServiceRequest addRequest = UserCigaretteChangeServiceRequest.builder()
			.changeType(ChangeType.ADD)
			.address("서울시 강남구 역삼동")
			.build();
		UserCigaretteChangeServiceRequest minusRequest = UserCigaretteChangeServiceRequest.builder()
			.changeType(ChangeType.MINUS)
			.build();

		// when: ADD
		userCigaretteService.changeCigarette(addRequest);

		// then: ADD 직후 최신 기록과 혜택은 모두 오늘을 기준으로 한다.
		assertThat(userCigaretteHistoryService.findLastestByUserId().getLastDate().toLocalDate())
			.isEqualTo(LocalDate.now());
		BenefitResponse afterAdd = benefitService.findMyBenefit();
		assertThat(afterAdd.getNonSmokingDays()).isZero();
		assertThat(afterAdd.getSavedMoney()).isZero();
		assertThat(afterAdd.getDailySavedMoney()).isEqualTo(5000L);

		// when: MINUS
		userCigaretteService.changeCigarette(minusRequest);

		// then: MINUS 직후 이전 Mongo 이력의 시각/주소와 혜택이 함께 복구된다.
		assertThat(userCigaretteHistoryService.findLastestByUserId().getLastDate()).isEqualTo(previousSmokeTime);
		assertThat(userCigaretteHistoryService.findLastestByUserId().getAddress()).isEqualTo("이전 주소");
		BenefitResponse afterMinus = benefitService.findMyBenefit();
		assertThat(afterMinus.getNonSmokingDays()).isEqualTo(3L);
		assertThat(afterMinus.getSavedMoney()).isEqualTo(15000L);
		assertThat(afterMinus.getDailySavedMoney()).isEqualTo(5000L);
	}
}
