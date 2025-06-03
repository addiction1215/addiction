package com.addiction.user.users.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.addiction.IntegrationTestSupport;
import com.addiction.survey.surveyAnswer.entity.SurveyAnswer;
import com.addiction.survey.surveyQuestion.entity.SurveyQuestion;
import com.addiction.survey.surveyResult.entity.SurveyResult;
import com.addiction.user.users.service.request.UserSaveServiceRequest;
import com.addiction.user.users.service.request.UserUpdatePurposeServiceRequest;
import com.addiction.user.users.service.request.UserUpdateServiceRequest;
import com.addiction.user.users.service.request.UserUpdateSurveyServiceRequest;
import com.addiction.user.users.service.response.UserUpdateSurveyResponse;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.Sex;
import com.addiction.user.users.entity.enums.SnsType;

public class UserServiceTest extends IntegrationTestSupport {

	@Autowired
	private UserService userService;

	@DisplayName("유저의 정보를 저장한다.")
	@Test
	void 유저의_정보를_저장한다() {
		//given
		UserSaveServiceRequest userSaveServiceRequest = UserSaveServiceRequest.builder()
			.email("test@test.com")
			.password("1234")
			.phoneNumber("01012341234")
			.build();

		//when
		userService.save(userSaveServiceRequest);

		//then
		assertThat(userRepository.findByEmail("test@test.com").get())
			.extracting("email", "phoneNumber")
			.contains("test@test.com", "01012341234");
	}

	@DisplayName("유저의 정보를 수정한다.")
	@Test
	void 유저의_정보를_수정한다() {
		//given
		User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);

		User savedUser = userRepository.save(user);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(savedUser.getId()));

		UserUpdateServiceRequest userUpdateServiceRequest = UserUpdateServiceRequest.builder()
			.sex(Sex.MAIL)
			.birthDay("12341234")
			.build();

		//when
		userService.update(userUpdateServiceRequest);

		//then
		assertThat(userRepository.findById(savedUser.getId()).get())
			.extracting("sex", "birthDay")
			.contains(Sex.MAIL, "12341234");
	}

	@DisplayName("유저의 설문조사 결과를 저장한다.")
	@Test
	void 유저의_설문조사_결과를_저장한다() {
		//given
		User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);

		User savedUser = userRepository.save(user);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(savedUser.getId()));

		//설문지 테스트 데이터
		SurveyQuestion surveyQuestion = createSurveyQuestion("현재 흡연 여부를 선택해주세요");
		surveyQuestionRepository.save(surveyQuestion);

		SurveyAnswer surveyAnswer1 = createSurveyAnswer(surveyQuestion, "현재 흡연 중이며, 이제 금연하고 싶어요.", 3);
		SurveyAnswer surveyAnswer2 = createSurveyAnswer(surveyQuestion, "현재 금연 중이며, 계속 유지하고 싶어요.", 2);

		surveyAnswerRepository.save(surveyAnswer1);
		surveyAnswerRepository.save(surveyAnswer2);

		//설문결과 테스트데이터
		SurveyResult surveyResult = createSurveyResult("라이트 스모커", 0);
		surveyResultRepository.save(surveyResult);
		surveyResultDescriptionRepository.save(createSurveyResultDescription(surveyResult, "지금이 가장 좋은 기회입니다."));
		surveyResultDescriptionRepository.save(
			createSurveyResultDescription(surveyResult, "아직 니코틴 의존도가 낮아 비교적 수월하게 금연할 수 있는 단계지만, 방심은 금물입니다."));

		UserUpdateSurveyServiceRequest userUpdateSurveyServiceRequest = UserUpdateSurveyServiceRequest.builder()
			.answerId(List.of(surveyAnswer1.getId()))
			.cigarettePrice(5000)
			.purpose("금연 화이팅")
			.build();

		//when
		UserUpdateSurveyResponse userUpdateSurveyResponse = userService.updateSurvey(userUpdateSurveyServiceRequest);

		//then
		assertAll(
			() -> assertThat(userUpdateSurveyResponse).extracting("nickName", "resultTitle")
				.contains("테스트 닉네임", "라이트 스모커"),
			() -> assertThat(userUpdateSurveyResponse.getResult()).isEqualTo(
				List.of("지금이 가장 좋은 기회입니다.", "아직 니코틴 의존도가 낮아 비교적 수월하게 금연할 수 있는 단계지만, 방심은 금물입니다."))
		);
	}

	@DisplayName("유저의 목표를 수정한다")
	@Test
	void 유저의_목표를_수정한다() {
		//given
		User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);

		User savedUser = userRepository.save(user);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(savedUser.getId()));

		userService.updatePurpose(UserUpdatePurposeServiceRequest.builder()
			.purpose("금연 화이팅")
			.build());

		//then
		assertThat(userRepository.findById(savedUser.getId()).get().getPurpose()).isEqualTo("금연 화이팅");
	}

}
