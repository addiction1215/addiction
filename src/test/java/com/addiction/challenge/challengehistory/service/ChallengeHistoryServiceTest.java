package com.addiction.challenge.challengehistory.service;

import com.addiction.IntegrationTestSupport;
import com.addiction.challenge.challange.entity.Challenge;
import com.addiction.challenge.challengehistory.controller.request.ChallengeJoinRequest;
import com.addiction.challenge.challengehistory.controller.request.ChallengeCompleteRequest;
import com.addiction.challenge.challengehistory.entity.ChallengeHistory;
import com.addiction.challenge.challengehistory.entity.ChallengeStatus;
import com.addiction.challenge.challengehistory.service.response.ChallengeCompleteResponse;
import com.addiction.challenge.challengehistory.service.response.FinishedChallengeHistoryResponse;
import com.addiction.challenge.mission.entity.Mission;
import com.addiction.challenge.mission.entity.MissionCategoryStatus;
import com.addiction.challenge.missionhistory.entity.MissionHistory;
import com.addiction.challenge.missionhistory.entity.MissionStatus;
import com.addiction.global.exception.AddictionException;
import com.addiction.global.page.request.PageInfoServiceRequest;
import com.addiction.global.page.response.PageCustom;
import com.addiction.jwt.dto.LoginUserInfo;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class ChallengeHistoryServiceTest extends IntegrationTestSupport {

    @Autowired
    private ChallengeHistoryService challengeHistoryService;

    @Autowired
    private ChallengeHistoryReadService challengeHistoryReadService;

    @DisplayName("이미 진행 중인 챌린지가 있으면 새로운 챌린지에 참여할 수 없다.")
    @Test
    void joinChallengeWhenProgressingChallengeExists() {
        // given
        User savedUser = userRepository.save(createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE));
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(LoginUserInfo.of(savedUser.getId()));

        Challenge progressingChallenge = cChallengeJpaRepository.save(
                createChallenge("진행 중 챌린지", "진행 중", "badge1.png", 100));
        challengeHistoryJpaRepository.save(
                createChallengeHistory(progressingChallenge, savedUser, ChallengeStatus.PROGRESSING));

        Challenge newChallenge = cChallengeJpaRepository.save(
                createChallenge("새 챌린지", "새 챌린지", "badge2.png", 200));
        ChallengeJoinRequest request = ChallengeJoinRequest.builder()
                .challengeId(newChallenge.getId())
                .build();

        // when // then
        assertThatThrownBy(() -> challengeHistoryService.joinChallenge(request))
                .isInstanceOf(AddictionException.class)
                .hasMessage("이미 진행 중인 챌린지가 있습니다.");
        assertThat(challengeHistoryJpaRepository.count()).isEqualTo(1);
    }

    @DisplayName("이미 완료한 챌린지에는 다시 참여할 수 없다.")
    @Test
    void joinChallengeWhenChallengeAlreadyCompleted() {
        // given
        User savedUser = userRepository.save(createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE));
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(LoginUserInfo.of(savedUser.getId()));

        Challenge challenge = cChallengeJpaRepository.save(
                createChallenge("완료한 챌린지", "완료", "badge.png", 100));
        challengeHistoryJpaRepository.save(
                createChallengeHistory(challenge, savedUser, ChallengeStatus.COMPLETED));

        ChallengeJoinRequest request = ChallengeJoinRequest.builder()
                .challengeId(challenge.getId())
                .build();

        // when // then
        assertThatThrownBy(() -> challengeHistoryService.joinChallenge(request))
                .isInstanceOf(AddictionException.class)
                .hasMessage("이미 완료한 챌린지입니다.");
        assertThat(challengeHistoryJpaRepository.count()).isEqualTo(1);
    }

    @DisplayName("취소한 챌린지에는 다시 참여할 수 있다.")
    @Test
    void joinChallengeWhenPreviousChallengeWasCancelled() {
        // given
        User savedUser = userRepository.save(createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE));
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(LoginUserInfo.of(savedUser.getId()));

        Challenge challenge = cChallengeJpaRepository.save(
                createChallenge("취소한 챌린지", "재참여", "badge.png", 100));
        challengeHistoryJpaRepository.save(
                createChallengeHistory(challenge, savedUser, ChallengeStatus.CANCELLED));

        ChallengeJoinRequest request = ChallengeJoinRequest.builder()
                .challengeId(challenge.getId())
                .build();

        // when
        challengeHistoryService.joinChallenge(request);

        // then
        assertThat(challengeHistoryJpaRepository.count()).isEqualTo(2);
        assertThat(challengeHistoryJpaRepository.existsByUserIdAndStatus(
                savedUser.getId(), ChallengeStatus.PROGRESSING)).isTrue();
    }

    @DisplayName("진행률이 100인 진행중 챌린지를 완료 처리한다.")
    @Test
    void completeChallenge() {
        // given
        User savedUser = userRepository.save(createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE));
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(LoginUserInfo.of(savedUser.getId()));
        given(s3StorageService.createPresignedUrl(any(), any()))
                .willReturn("test-presigned-url");

        ChallengeHistory savedChallengeHistory = saveChallengeHistoryWithMissions(savedUser, ChallengeStatus.PROGRESSING,
                MissionStatus.COMPLETED, MissionStatus.COMPLETED);

        ChallengeCompleteRequest request = ChallengeCompleteRequest.builder()
                .challengeHistoryId(savedChallengeHistory.getId())
                .build();

        // when
        ChallengeCompleteResponse response = challengeHistoryService.completeChallenge(request);

        // then
        assertThat(response.getChallengeHistoryId()).isEqualTo(savedChallengeHistory.getId());
        assertThat(response.getStatus()).isEqualTo(ChallengeStatus.COMPLETED);

        ChallengeHistory foundChallengeHistory = challengeHistoryJpaRepository.findById(savedChallengeHistory.getId()).orElseThrow();
        assertThat(foundChallengeHistory.getStatus()).isEqualTo(ChallengeStatus.COMPLETED);

        PageCustom<FinishedChallengeHistoryResponse> finishedResponse = challengeHistoryReadService.getFinishedChallengeList(
                PageInfoServiceRequest.builder()
                        .page(1)
                        .size(10)
                        .build()
        );

        assertThat(finishedResponse.getContent())
                .extracting(FinishedChallengeHistoryResponse::getChallengeHistoryId)
                .contains(savedChallengeHistory.getId());
    }

    @DisplayName("본인의 챌린지가 아니면 완료 처리할 수 없다.")
    @Test
    void completeChallengeWithOtherUser() {
        // given
        User savedUser = userRepository.save(createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE));
        User otherUser = userRepository.save(createUser("other@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE));
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(LoginUserInfo.of(otherUser.getId()));

        ChallengeHistory savedChallengeHistory = saveChallengeHistoryWithMissions(savedUser, ChallengeStatus.PROGRESSING,
                MissionStatus.COMPLETED);

        ChallengeCompleteRequest request = ChallengeCompleteRequest.builder()
                .challengeHistoryId(savedChallengeHistory.getId())
                .build();

        // when // then
        assertThatThrownBy(() -> challengeHistoryService.completeChallenge(request))
                .isInstanceOf(AddictionException.class)
                .hasMessage("본인의 챌린지만 완료할 수 있습니다.");
    }

    @DisplayName("진행중 상태가 아니면 완료 처리할 수 없다.")
    @Test
    void completeChallengeWhenNotProgressing() {
        // given
        User savedUser = userRepository.save(createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE));
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(LoginUserInfo.of(savedUser.getId()));

        for (ChallengeStatus status : List.of(ChallengeStatus.COMPLETED, ChallengeStatus.FAILED, ChallengeStatus.CANCELLED)) {
            ChallengeHistory savedChallengeHistory = saveChallengeHistoryWithMissions(savedUser, status,
                    MissionStatus.COMPLETED);

            ChallengeCompleteRequest request = ChallengeCompleteRequest.builder()
                    .challengeHistoryId(savedChallengeHistory.getId())
                    .build();

            // when // then
            assertThatThrownBy(() -> challengeHistoryService.completeChallenge(request))
                    .isInstanceOf(AddictionException.class)
                    .hasMessage("진행중인 챌린지만 완료할 수 있습니다.");
        }
    }

    @DisplayName("PROGRESSING 상태의 미션이 남아 있으면 챌린지를 완료 처리할 수 없다.")
    @Test
    void completeChallengeWhenProgressingMissionExists() {
        // given
        User savedUser = userRepository.save(createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE));
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(LoginUserInfo.of(savedUser.getId()));

        ChallengeHistory savedChallengeHistory = saveChallengeHistoryWithMissions(savedUser, ChallengeStatus.PROGRESSING,
                MissionStatus.COMPLETED, MissionStatus.PROGRESSING);

        ChallengeCompleteRequest request = ChallengeCompleteRequest.builder()
                .challengeHistoryId(savedChallengeHistory.getId())
                .build();

        // when // then
        assertThatThrownBy(() -> challengeHistoryService.completeChallenge(request))
                .isInstanceOf(AddictionException.class)
                .hasMessage("모든 미션이 완료된 챌린지만 완료할 수 있습니다.");
    }

    @DisplayName("READY 상태의 미션이 남아 있으면 챌린지를 완료 처리할 수 없다.")
    @Test
    void completeChallengeWhenReadyMissionExists() {
        // given
        User savedUser = userRepository.save(createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE));
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(LoginUserInfo.of(savedUser.getId()));

        ChallengeHistory savedChallengeHistory = saveChallengeHistoryWithMissions(savedUser, ChallengeStatus.PROGRESSING,
                MissionStatus.COMPLETED, MissionStatus.READY);

        ChallengeCompleteRequest request = ChallengeCompleteRequest.builder()
                .challengeHistoryId(savedChallengeHistory.getId())
                .build();

        // when // then
        assertThatThrownBy(() -> challengeHistoryService.completeChallenge(request))
                .isInstanceOf(AddictionException.class)
                .hasMessage("모든 미션이 완료된 챌린지만 완료할 수 있습니다.");
    }

    @DisplayName("존재하지 않는 챌린지 이력이면 완료 처리할 수 없다.")
    @Test
    void completeChallengeWithNonExistentChallengeHistory() {
        // given
        User savedUser = userRepository.save(createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE));
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(LoginUserInfo.of(savedUser.getId()));

        ChallengeCompleteRequest request = ChallengeCompleteRequest.builder()
                .challengeHistoryId(999L)
                .build();

        // when // then
        assertThatThrownBy(() -> challengeHistoryService.completeChallenge(request))
                .isInstanceOf(AddictionException.class)
                .hasMessage("존재하지 않는 챌린지 이력입니다.");
    }

    private ChallengeHistory saveChallengeHistoryWithMissions(User user, ChallengeStatus challengeStatus, MissionStatus... missionStatuses) {
        Challenge challenge = cChallengeJpaRepository.save(createChallenge("금연 챌린지", "금연하기", "badge.png", 100));
        ChallengeHistory challengeHistory = challengeHistoryJpaRepository.save(createChallengeHistory(challenge, user, challengeStatus));

        for (int i = 0; i < missionStatuses.length; i++) {
            Mission mission = missionJpaRepository.save(createMission(challenge, MissionCategoryStatus.REPLACE_ACTION,
                    "미션" + i, "미션 내용" + i, 10));
            MissionHistory missionHistory = createMissionHistory(challengeHistory, mission, user, missionStatuses[i], null);
            missionHistoryJpaRepository.save(missionHistory);
        }

        return challengeHistory;
    }
}
