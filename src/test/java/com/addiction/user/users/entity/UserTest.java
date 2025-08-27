package com.addiction.user.users.entity;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.addiction.IntegrationTestSupport;
import com.addiction.global.exception.AddictionException;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.Sex;
import com.addiction.user.users.entity.enums.SnsType;

public class UserTest extends IntegrationTestSupport {

	@DisplayName("로그인 타입을 검증한다.")
	@Test
	void 로그인_타입을_검증한다() {
		User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.INCOMPLETE);
		user.checkSnsType(SnsType.NORMAL);
	}

	@DisplayName("로그인 타입을 검증시 이미 다른 타입의 회원가입 타입이면 예외가 발생한다.")
	@Test
	void 로그인_타입을_검증시_이미_다른_타입의_회원가입_타입이면_예외가_발생한다() {
		User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.INCOMPLETE);

		assertThatThrownBy(() -> user.checkSnsType(SnsType.KAKAO))
			.isInstanceOf(AddictionException.class)
			.hasMessage("NORMAL");
	}

	@DisplayName("회원 정보를 수정한다.")
	@Test
	void 회원_정보를_수정한다() {
		User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.INCOMPLETE);

		user.update(Sex.FEMAIL, "12341234");

		assertThat(user).extracting("sex", "birthDay")
			.contains(Sex.FEMAIL, "12341234");
	}

	@DisplayName("회원 설문조사 결과를 수정한다.")
	@Test
	void 회원_설문조사_결과를_수정한다() {
		User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.INCOMPLETE);

		LocalDateTime startDate = LocalDateTime.now();

		user.updateSurvey("금연 화이팅", 10, 5000, startDate);

		assertThat(user).extracting( "purpose", "totalScore", "cigarettePrice", "settingStatus", "startDate")
			.contains("금연 화이팅", 10, 5000, SettingStatus.COMPLETE, startDate);
	}

	@DisplayName("회원 목표 수정한다.")
	@Test
	void 회원_목표를_수정한다() {
		User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.INCOMPLETE);
		user.updatePurpose("금연 화이팅");

		assertThat(user.getPurpose()).isEqualTo("금연 화이팅");
	}
}
