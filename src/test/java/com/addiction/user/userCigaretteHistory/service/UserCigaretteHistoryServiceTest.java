
package com.addiction.user.userCigaretteHistory.service;

import com.addiction.IntegrationTestSupport;
import com.addiction.user.userCigarette.service.UserCigaretteService;
import com.addiction.user.userCigarette.service.request.ChangeType;
import com.addiction.user.userCigarette.service.request.UserCigaretteChangeServiceRequest;
import com.addiction.user.userCigaretteHistory.service.response.UserCigaretteHistoryLastestResponse;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class UserCigaretteHistoryServiceTest extends IntegrationTestSupport {

	@Autowired
	private UserCigaretteService userCigaretteService;
    @Autowired
    private UserCigaretteHistoryService userCigaretteHistoryService;


	@DisplayName("유저의 마지막 흡연 기록을 조회한다.")
	@Test
	void 유저의_마지막_흡연_기록을_조회한다() {
		// given
		User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);
		userRepository.save(user);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		// when
		// then
        UserCigaretteChangeServiceRequest request1 = UserCigaretteChangeServiceRequest.builder()
                .changeType(ChangeType.ADD)
                .address("서울시 강남구")
                .build();
        userCigaretteService.changeCigarette(request1);

        UserCigaretteChangeServiceRequest request2 = UserCigaretteChangeServiceRequest.builder()
                .changeType(ChangeType.ADD)
                .address("서울시 송파구")
                .build();
        userCigaretteService.changeCigarette(request2);

        // when
        UserCigaretteHistoryLastestResponse result = userCigaretteHistoryService.findLastestByUserId();

        // then
        assertThat(result)
                .extracting("address")
                .isEqualTo("서울시 송파구");
	}
}
