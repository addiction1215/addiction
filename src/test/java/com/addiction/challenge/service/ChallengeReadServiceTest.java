package com.addiction.challenge.service;

import com.addiction.IntegrationTestSupport;
import com.addiction.challenge.entity.Challenge;
import com.addiction.challenge.service.challenge.response.ChallengeResponseList;
import com.addiction.common.enums.YnStatus;
import com.addiction.global.page.request.PageInfoServiceRequest;
import com.addiction.global.page.response.PageCustom;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class ChallengeReadServiceTest extends IntegrationTestSupport {

    @Autowired
    private ChallengeReadService challengeReadService;

    @DisplayName("챌린지 목록 조회")
    @Test
    void 챌린지_목록_조회() {
        //given
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.INCOMPLETE);
        userRepository.save(user);

        Challenge challenge = createChallenge(user);
        Challenge savedChallenge = challengeRepository.save(challenge);

        YnStatus status = YnStatus.Y;

        PageInfoServiceRequest request = PageInfoServiceRequest.builder()
                .page(1)
                .size(10)
                .build();

        ChallengeResponseList response = ChallengeResponseList.of(savedChallenge);

        // when
        PageCustom<ChallengeResponseList> result = challengeReadService.getChallenge(status, request);

        // then
        assertThat(result.getContent()).extracting("id", "title", "content")
                .contains(
                        tuple(response.getChallengeId(), response.getTitle(), response.getContent())
                );

        assertThat(result.getPageInfo()).extracting("currentPage", "totalPage", "totalElement")
                .contains(1, 1, 1);

    }
}
