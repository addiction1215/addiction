package com.addiction.challenge.challengehistory.service;

import com.addiction.challenge.challenge.entity.Challenge;
import com.addiction.challenge.challengehistory.repository.ChallengeHistoryRepository;
import com.addiction.challenge.challengehistory.repository.response.ChallengeHistoryUserDto;
import com.addiction.challenge.challengehistory.service.impl.ChallengeHistoryReadServiceImpl;
import com.addiction.challenge.challengehistory.service.response.ChallengeHistoryUserResponse;
import com.addiction.global.security.SecurityService;
import com.addiction.jwt.dto.LoginUserInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ChallengeHistoryReadServiceTest {

    @InjectMocks
    private ChallengeHistoryReadServiceImpl challengeHistoryReadService;

    @Mock
    private SecurityService securityService;

    @Mock
    private ChallengeHistoryRepository challengeHistoryRepository;

    @DisplayName("유저의 챌린지 이력을 조회한다.")
    @Test
    void 유저의_챌린지_이력을_조회한다() {
        // given
        Long userId = 1L;
        LoginUserInfo loginUserInfo = LoginUserInfo.of(userId);

        Challenge challenge1 = Challenge.builder().id(1L).title("challenge1").badge("badge1").build();
        Challenge challenge2 = Challenge.builder().id(2L).title("challenge2").badge("badge2").build();

        given(securityService.getCurrentLoginUserInfo()).willReturn(loginUserInfo);
        given(challengeHistoryRepository.findByUserId(userId)).willReturn(List.of(
                new ChallengeHistoryUserDto(challenge1),
                new ChallengeHistoryUserDto(challenge2)
        ));

        // when
        List<ChallengeHistoryUserResponse> responses = challengeHistoryReadService.findByUserId();

        // then
        assertThat(responses).hasSize(2)
                .extracting("challengeId", "title", "badge")
                .containsExactlyInAnyOrder(
                        tuple(1L, "challenge1", "badge1"),
                        tuple(2L, "challenge2", "badge2")
                );
    }
}
