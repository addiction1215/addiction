package com.addiction.user.userCigarette.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.addiction.IntegrationTestSupport;

public class UserCigaretteTest extends IntegrationTestSupport {

	@DisplayName("사용자의 담배 갯수를 증가한다")
	@Test
	void 사용자_담배_갯수_증가() {
		// given
		UserCigarette userCigarette = UserCigarette.builder()
			.count(0)
			.build();

		// when
		userCigarette.addCount();

		// then
		assertThat(userCigarette.getCount()).isEqualTo(1);
	}

	@DisplayName("사용자의 담배 갯수를 감소한다")
	@Test
	void 사용자_담배_갯수_감소() {
		// given
		UserCigarette userCigarette = UserCigarette.builder()
			.count(1)
			.build();

		// when
		userCigarette.minusCount();

		// then
		assertThat(userCigarette.getCount()).isEqualTo(0);
	}
}
