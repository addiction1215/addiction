package com.addiction.challenge.service;

import com.addiction.IntegrationTestSupport;
import com.addiction.challenge.challenge.entity.Challenge;
import com.addiction.challenge.challenge.service.ChallengeReadService;
import com.addiction.challenge.challenge.service.challenge.response.ChallengeResponse;
import com.addiction.challenge.challenge.service.response.ChallengeDetailResponse;
import com.addiction.challenge.challengehistory.entity.ChallengeHistory;
import com.addiction.common.enums.YnStatus;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ChallengeReadServiceTest extends IntegrationTestSupport {

    @Autowired
    private ChallengeReadService challengeReadService;

    @DisplayName("챌린지 목록 조회")
    @Test
    void 챌린지_목록_조회() {
        //given
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.INCOMPLETE);
        userRepository.save(user);

        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createLoginUserInfo(user.getId()));

        Challenge challenge1 = createChallenge(user, "완료 챌린지 1");
        Challenge challenge2 = createChallenge(user, "완료 챌린지 2");
        Challenge challenge3 = createChallenge(user, "미완료 챌린지 1");
        Challenge challenge4 = createChallenge(user, "미완료 챌린지 2");

        cChallengeJpaRepository.saveAll(List.of(challenge1, challenge2, challenge3, challenge4));

        ChallengeHistory history1 = createChallengeHistory(user, challenge1, YnStatus.Y);
        ChallengeHistory history2 = createChallengeHistory(user, challenge2, YnStatus.Y);
        ChallengeHistory history3 = createChallengeHistory(user, challenge3, YnStatus.N);
        ChallengeHistory history4 = createChallengeHistory(user, challenge4, YnStatus.N);
        challengeHistoryJpaRepository.saveAll(List.of(history1, history2, history3, history4));

        // when
        ChallengeResponse result = challengeReadService.getChallenge(null);

        // then
        assertThat(result).isNotNull();

        assertThat(result.getFinishedChallengeList()).hasSize(2)
                .extracting("title", "finishYn")
                .containsExactlyInAnyOrder(
                        tuple("완료 챌린지 1", YnStatus.Y),
                        tuple("완료 챌린지 2", YnStatus.Y)
                );

        assertThat(result.getLeftChallengeList()).hasSize(2)
                .extracting("title", "finishYn")
                .containsExactlyInAnyOrder(
                        tuple("미완료 챌린지 1", YnStatus.N),
                        tuple("미완료 챌린지 2", YnStatus.N)
                );
    }

    @DisplayName("챌린지 ID를 받아 챌린지 상세 정보를 조회한다.")
    @Test
    void 챌린지_ID를_받아_챌린지_상세_정보를_조회한다() {
        // given
        Challenge challenge = Challenge.builder()
                .badge("badge_url")
                .title("챌린지 제목")
                .content("챌린지 내용")
                .build();

        challengeRepository.save(challenge);

        // when
        ChallengeDetailResponse response = challengeReadService.findById(challenge.getId());

        // then
        assertThat(response)
                .extracting("badge", "title", "content")
                .contains("badge_url", "챌린지 제목", "챌린지 내용");
    }

    @DisplayName("존재하지 않는 챌린지 ID로 상세 정보를 조회하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_챌린지_ID로_상세_정보를_조회하면_예외가_발생한다() {
        // given
        Long nonExistingChallengeId = 999L;
        // when // then
        assertThatThrownBy(() -> challengeReadService.findById(nonExistingChallengeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 챌린지는 없습니다. id = " + nonExistingChallengeId);
    }

    private Challenge createChallenge(User user, String title) {
        return Challenge.builder()
                .content("test content")
                .title(title)
                .badge("test badge")
                .user(user)
                .build();
    }
}
