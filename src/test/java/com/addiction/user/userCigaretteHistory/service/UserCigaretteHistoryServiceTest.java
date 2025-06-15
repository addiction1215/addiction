package com.addiction.user.userCigaretteHistory.service;


import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.addiction.IntegrationTestSupport;
import com.addiction.user.userCigaretteHistory.repository.UserCigaretteHistoryRepository;
import com.addiction.user.userCigaretteHistory.service.request.UserCigaretteHistoryServiceRequest;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;

public class UserCigaretteHistoryServiceTest extends IntegrationTestSupport {

	@Autowired
	private UserCigaretteHistoryService userCigaretteHistoryService;

	@DisplayName("유저의 흡연 기록을 저장한다.")
	@Test
	void 유저의_흡연_기록을_저장한다() {
		// given
		User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
		userRepository.save(user);

		UserCigaretteHistoryServiceRequest request = UserCigaretteHistoryServiceRequest.builder()
			.user(user)
			.count(5)
			.build();

		// when
		Long id = userCigaretteHistoryService.save(request);

		// then
		assertThat(userCigaretteHistoryRepository.findById(id).get().getCount())
			.isEqualTo(5);
	}

}
