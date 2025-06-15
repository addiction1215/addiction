package com.addiction.user.userCigarette.service;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.addiction.IntegrationTestSupport;
import com.addiction.user.userCigarette.entity.UserCigarette;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;

public class UserCigaretteReadServiceTest extends IntegrationTestSupport {

	@Autowired
	private UserCigaretteReadService userCigaretteReadService;

	@DisplayName("사용자의 담배 개수를 조회한다.")
	@Test
	void 사용자_담배_개수_조회() {
		// given
		User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
		userRepository.save(user);

		// when
		UserCigarette cigarette = userCigaretteReadService.findByUserId(user.getId());

		// then
		assertThat(cigarette.getCount()).isEqualTo(0);
	}
}
