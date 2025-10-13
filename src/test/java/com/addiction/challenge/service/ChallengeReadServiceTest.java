package com.addiction.challenge.service;

import com.addiction.IntegrationTestSupport;
import com.addiction.challenge.entity.Challenge;
import com.addiction.challenge.service.challenge.response.ChallengeResponse;
import com.addiction.common.enums.YnStatus;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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

        Challenge challenge1 = createChallenge(user, "완료 챌린지 1");
        Challenge challenge2 = createChallenge(user, "완료 챌린지 2");
        Challenge challenge3 = createChallenge(user, "미완료 챌린지 1");
        Challenge challenge4 = createChallenge(user, "미완료 챌린지 2");
        cChallengeJpaRepository.saveAll(List.of(challenge1, challenge2, challenge3, challenge4));

//        ChallengeHistory history1 = createChallengeHistory(user, challenge1, YnStatus.Y);
//        ChallengeHistory history2 = createChallengeHistory(user, challenge2, YnStatus.Y);
//        ChallengeHistory history3 = createChallenge_history(user, challenge3, YnStatus.N);
//        ChallengeHistory history4 = createChallengeHistory(user, challenge4, YnStatus.N);
//        challengeHistoryRepository.saveAll(List.of(history1, history2, history3, history4));

        // when
        ChallengeResponse result = challengeReadService.getChallenge(null);

        // then
        assertThat(result).isNotNull();

        assertThat(result.getFinishedChallengeList()).hasSize(2)
                .extracting("title", "finishYn")
                .containsExactlyInAnyOrder(
                        tuple("완료 챌린지 1", YnStatus.Y.toString()),
                        tuple("완료 챌린지 2", YnStatus.Y.toString())
                );

        assertThat(result.getLeftChallengeList()).hasSize(2)
                .extracting("title", "finishYn")
                .containsExactlyInAnyOrder(
                        tuple("미완료 챌린지 1", YnStatus.N.toString()),
                        tuple("미완료 챌린지 2", YnStatus.N.toString())
                );
    }

    private Challenge createChallenge(User user, String title) {
        return Challenge.builder()
                .content("test content")
                .title(title)
                .badge("test badge")
                .userId(user)
                .build();
    }
}
