package com.addiction.challenge.challengehistory.repository;

import com.addiction.IntegrationTestSupport;
import com.addiction.challenge.challenge.entity.Challenge;
import com.addiction.challenge.challenge.repository.ChallengeJpaRepository;
import com.addiction.challenge.challengehistory.entity.ChallengeHistory;
import com.addiction.challenge.challengehistory.repository.response.ChallengeHistoryUserDto;
import com.addiction.common.enums.YnStatus;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SnsType;
import com.addiction.user.users.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class ChallengeHistoryQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ChallengeHistoryQueryRepository challengeHistoryQueryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChallengeJpaRepository challengeJpaRepository;

    @Autowired
    private ChallengeHistoryJpaRepository challengeHistoryJpaRepository;

    @DisplayName("유저의 챌린지 이력을 조회한다.")
    @Test
    void 유저의_챌린지_이력을_조회한다() {
        // given
        User user = User.builder()
                .email("test@example.com")
                .snsType(SnsType.NORMAL)
                .build();
        userRepository.save(user);

        Challenge challenge1 = Challenge.builder().title("challenge1").badge("badge1").build();
        Challenge challenge2 = Challenge.builder().title("challenge2").badge("badge2").build();
        Challenge challenge3 = Challenge.builder().title("challenge3").badge("badge3").build();
        challengeJpaRepository.saveAll(List.of(challenge1, challenge2, challenge3));

        ChallengeHistory history1 = ChallengeHistory.builder().user(user).challenge(challenge1).finishYn(YnStatus.Y).build();
        ChallengeHistory history2 = ChallengeHistory.builder().user(user).challenge(challenge2).finishYn(YnStatus.Y).build();
        challengeHistoryJpaRepository.saveAll(List.of(history1, history2));

        // when
        List<ChallengeHistoryUserDto> results = challengeHistoryQueryRepository.findByUserId(user.getId());

        // then
        assertThat(results).hasSize(2)
                .extracting("title", "badge")
                .containsExactlyInAnyOrder(
                        tuple("challenge1", "badge1"),
                        tuple("challenge2", "badge2")
                );
    }
}
