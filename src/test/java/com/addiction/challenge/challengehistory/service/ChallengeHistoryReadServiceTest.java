package com.addiction.challenge.challengehistory.service;

import com.addiction.IntegrationTestSupport;
import com.addiction.challenge.challange.entity.Challenge;
import com.addiction.challenge.challengehistory.entity.ChallengeHistory;
import com.addiction.challenge.challengehistory.entity.ChallengeStatus;
import com.addiction.challenge.challengehistory.service.response.ChallengeHistoryResponse;
import com.addiction.global.page.request.PageInfoServiceRequest;
import com.addiction.global.page.response.PageCustom;
import com.addiction.jwt.dto.LoginUserInfo;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.BDDMockito.given;

class ChallengeHistoryReadServiceTest extends IntegrationTestSupport {

    @Autowired
    private ChallengeHistoryReadService challengeHistoryReadService;

    @DisplayName("진행중인 챌린지를 조회한다.")
    @Test
    void getProgressingChallenge() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        User savedUser = userRepository.save(user);

        // SecurityService Mock 설정
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(LoginUserInfo.of(savedUser.getId()));

        Challenge challenge = createChallenge("금연 챌린지", "30일 금연 도전", "badge1.png", 500);
        Challenge savedChallenge = cChallengeJpaRepository.save(challenge);

        ChallengeHistory challengeHistory = createChallengeHistory(savedChallenge, savedUser, ChallengeStatus.PROGRESSING);
        ChallengeHistory savedChallengeHistory = challengeHistoryJpaRepository.save(challengeHistory);

        // when
        ChallengeHistoryResponse response = challengeHistoryReadService.getProgressingChallenge();

        // then
        // ChallengeHistoryResponse 모든 필드(6개) 검증
        assertThat(response).isNotNull();
        assertThat(response.getChallengeHistoryId()).isEqualTo(savedChallengeHistory.getId());
        assertThat(response.getTitle()).isEqualTo("금연 챌린지");
        assertThat(response.getContent()).isEqualTo("30일 금연 도전");
        assertThat(response.getBadge()).isEqualTo("badge1.png");
        assertThat(response.getReward()).isEqualTo(500);
        assertThat(response.getStatus()).isEqualTo(ChallengeStatus.PROGRESSING);
    }

    @DisplayName("진행중인 챌린지가 없으면 null을 반환한다.")
    @Test
    void getProgressingChallengeWhenNull() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        User savedUser = userRepository.save(user);

        // SecurityService Mock 설정
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(LoginUserInfo.of(savedUser.getId()));

        Challenge challenge = createChallenge("완료된 챌린지", "이미 완료됨", "badge1.png", 300);
        Challenge savedChallenge = cChallengeJpaRepository.save(challenge);

        // 완료된 챌린지만 있음
        ChallengeHistory completedHistory = createChallengeHistory(savedChallenge, savedUser, ChallengeStatus.COMPLETED);
        challengeHistoryJpaRepository.save(completedHistory);

        // when
        ChallengeHistoryResponse response = challengeHistoryReadService.getProgressingChallenge();

        // then
        assertThat(response).isNull();
    }

    @DisplayName("단일 진행중 챌린지의 모든 필드가 정확히 매핑된다.")
    @Test
    void getProgressingChallengeWithAllFields() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        User savedUser = userRepository.save(user);

        // SecurityService Mock 설정
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(LoginUserInfo.of(savedUser.getId()));

        Challenge challenge = createChallenge("테스트 챌린지", "테스트 챌린지 상세 내용", "test_badge.png", 999);
        Challenge savedChallenge = cChallengeJpaRepository.save(challenge);

        ChallengeHistory challengeHistory = createChallengeHistory(savedChallenge, savedUser, ChallengeStatus.PROGRESSING);
        ChallengeHistory savedChallengeHistory = challengeHistoryJpaRepository.save(challengeHistory);

        // when
        ChallengeHistoryResponse response = challengeHistoryReadService.getProgressingChallenge();

        // then
        // ChallengeHistoryResponse 모든 필드(6개) 검증
        assertThat(response).isNotNull();
        assertThat(response.getChallengeHistoryId()).isEqualTo(savedChallengeHistory.getId());
        assertThat(response.getTitle()).isEqualTo("테스트 챌린지");
        assertThat(response.getContent()).isEqualTo("테스트 챌린지 상세 내용");
        assertThat(response.getBadge()).isEqualTo("test_badge.png");
        assertThat(response.getReward()).isEqualTo(999);
        assertThat(response.getStatus()).isEqualTo(ChallengeStatus.PROGRESSING);
    }

    @DisplayName("완료된 챌린지 목록을 페이징하여 조회한다.")
    @Test
    void getFinishedChallengeList() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        User savedUser = userRepository.save(user);

        // SecurityService Mock 설정
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(LoginUserInfo.of(savedUser.getId()));

        Challenge challenge1 = createChallenge("챌린지1", "내용1", "badge1.png", 100);
        Challenge challenge2 = createChallenge("챌린지2", "내용2", "badge2.png", 200);
        Challenge challenge3 = createChallenge("챌린지3", "내용3", "badge3.png", 300);
        Challenge challenge4 = createChallenge("챌린지4", "내용4", "badge4.png", 400);

        Challenge savedChallenge1 = cChallengeJpaRepository.save(challenge1);
        Challenge savedChallenge2 = cChallengeJpaRepository.save(challenge2);
        Challenge savedChallenge3 = cChallengeJpaRepository.save(challenge3);
        Challenge savedChallenge4 = cChallengeJpaRepository.save(challenge4);

        // 완료된 챌린지 3개, 진행중 챌린지 1개
        ChallengeHistory history1 = createChallengeHistory(savedChallenge1, savedUser, ChallengeStatus.COMPLETED);
        ChallengeHistory history2 = createChallengeHistory(savedChallenge2, savedUser, ChallengeStatus.COMPLETED);
        ChallengeHistory history3 = createChallengeHistory(savedChallenge3, savedUser, ChallengeStatus.COMPLETED);
        ChallengeHistory history4 = createChallengeHistory(savedChallenge4, savedUser, ChallengeStatus.PROGRESSING);

        ChallengeHistory savedHistory1 = challengeHistoryJpaRepository.save(history1);
        ChallengeHistory savedHistory2 = challengeHistoryJpaRepository.save(history2);
        ChallengeHistory savedHistory3 = challengeHistoryJpaRepository.save(history3);
        challengeHistoryJpaRepository.save(history4);

        PageInfoServiceRequest pageRequest = PageInfoServiceRequest.builder()
                .page(1)
                .size(10)
                .build();

        // when
        PageCustom<ChallengeHistoryResponse> response = challengeHistoryReadService.getFinishedChallengeList(pageRequest);

        // then
        // PageCustom 검증
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(3); // 완료된 챌린지만 3개
        assertThat(response.getPageInfo()).isNotNull();

        // PageableCustom 모든 필드(3개) 검증
        assertThat(response.getPageInfo().getCurrentPage()).isEqualTo(1);
        assertThat(response.getPageInfo().getTotalPage()).isEqualTo(1);
        assertThat(response.getPageInfo().getTotalElement()).isEqualTo(3);

        // ChallengeHistoryResponse 모든 필드(6개) 검증
        assertThat(response.getContent())
                .extracting("challengeHistoryId", "title", "content", "badge", "reward", "status")
                .containsExactlyInAnyOrder(
                        tuple(savedHistory1.getId(), "챌린지1", "내용1", "badge1.png", 100, ChallengeStatus.COMPLETED),
                        tuple(savedHistory2.getId(), "챌린지2", "내용2", "badge2.png", 200, ChallengeStatus.COMPLETED),
                        tuple(savedHistory3.getId(), "챌린지3", "내용3", "badge3.png", 300, ChallengeStatus.COMPLETED)
                );
    }

    @DisplayName("완료된 챌린지가 없는 경우 빈 목록을 반환한다.")
    @Test
    void getFinishedChallengeListWhenEmpty() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        User savedUser = userRepository.save(user);

        // SecurityService Mock 설정
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(LoginUserInfo.of(savedUser.getId()));

        Challenge challenge = createChallenge("진행중 챌린지", "진행중", "badge1.png", 100);
        Challenge savedChallenge = cChallengeJpaRepository.save(challenge);

        // 진행중인 챌린지만 있음
        ChallengeHistory progressingHistory = createChallengeHistory(savedChallenge, savedUser, ChallengeStatus.PROGRESSING);
        challengeHistoryJpaRepository.save(progressingHistory);

        PageInfoServiceRequest pageRequest = PageInfoServiceRequest.builder()
                .page(1)
                .size(10)
                .build();

        // when
        PageCustom<ChallengeHistoryResponse> response = challengeHistoryReadService.getFinishedChallengeList(pageRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isEmpty();
        assertThat(response.getPageInfo().getCurrentPage()).isEqualTo(1);
        assertThat(response.getPageInfo().getTotalPage()).isEqualTo(0);
        assertThat(response.getPageInfo().getTotalElement()).isEqualTo(0);
    }

    @DisplayName("페이징이 정상적으로 동작한다 - 2페이지 조회")
    @Test
    void getFinishedChallengeListWithPagination() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        User savedUser = userRepository.save(user);

        // SecurityService Mock 설정
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(LoginUserInfo.of(savedUser.getId()));

        // 15개의 완료된 챌린지 생성
        for (int i = 1; i <= 15; i++) {
            Challenge challenge = createChallenge("챌린지" + i, "내용" + i, "badge" + i + ".png", i * 100);
            Challenge savedChallenge = cChallengeJpaRepository.save(challenge);
            ChallengeHistory history = createChallengeHistory(savedChallenge, savedUser, ChallengeStatus.COMPLETED);
            challengeHistoryJpaRepository.save(history);
        }

        PageInfoServiceRequest pageRequest = PageInfoServiceRequest.builder()
                .page(2)
                .size(5)
                .build();

        // when
        PageCustom<ChallengeHistoryResponse> response = challengeHistoryReadService.getFinishedChallengeList(pageRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(5);
        assertThat(response.getPageInfo().getCurrentPage()).isEqualTo(2);
        assertThat(response.getPageInfo().getTotalPage()).isEqualTo(3); // 15개 / 5 = 3페이지
        assertThat(response.getPageInfo().getTotalElement()).isEqualTo(15);
    }

    @DisplayName("페이징 - 마지막 페이지 조회")
    @Test
    void getFinishedChallengeListLastPage() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        User savedUser = userRepository.save(user);

        // SecurityService Mock 설정
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(LoginUserInfo.of(savedUser.getId()));

        // 12개의 완료된 챌린지 생성
        for (int i = 1; i <= 12; i++) {
            Challenge challenge = createChallenge("챌린지" + i, "내용" + i, "badge" + i + ".png", i * 100);
            Challenge savedChallenge = cChallengeJpaRepository.save(challenge);
            ChallengeHistory history = createChallengeHistory(savedChallenge, savedUser, ChallengeStatus.COMPLETED);
            challengeHistoryJpaRepository.save(history);
        }

        PageInfoServiceRequest pageRequest = PageInfoServiceRequest.builder()
                .page(3)
                .size(5)
                .build();

        // when
        PageCustom<ChallengeHistoryResponse> response = challengeHistoryReadService.getFinishedChallengeList(pageRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(2); // 마지막 페이지는 2개만
        assertThat(response.getPageInfo().getCurrentPage()).isEqualTo(3);
        assertThat(response.getPageInfo().getTotalPage()).isEqualTo(3);
        assertThat(response.getPageInfo().getTotalElement()).isEqualTo(12);
    }

    @DisplayName("단일 완료된 챌린지의 모든 필드가 정확히 매핑된다.")
    @Test
    void getSingleFinishedChallengeWithAllFields() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        User savedUser = userRepository.save(user);

        // SecurityService Mock 설정
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(LoginUserInfo.of(savedUser.getId()));

        Challenge challenge = createChallenge("테스트 완료 챌린지", "테스트 완료 챌린지 상세", "complete_badge.png", 777);
        Challenge savedChallenge = cChallengeJpaRepository.save(challenge);

        ChallengeHistory history = createChallengeHistory(savedChallenge, savedUser, ChallengeStatus.COMPLETED);
        ChallengeHistory savedHistory = challengeHistoryJpaRepository.save(history);

        PageInfoServiceRequest pageRequest = PageInfoServiceRequest.builder()
                .page(1)
                .size(10)
                .build();

        // when
        PageCustom<ChallengeHistoryResponse> response = challengeHistoryReadService.getFinishedChallengeList(pageRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);

        // ChallengeHistoryResponse 모든 필드(6개) 검증
        assertThat(response.getContent())
                .extracting("challengeHistoryId", "title", "content", "badge", "reward", "status")
                .containsExactly(
                        tuple(savedHistory.getId(), "테스트 완료 챌린지", "테스트 완료 챌린지 상세", "complete_badge.png", 777, ChallengeStatus.COMPLETED)
                );
    }

    @DisplayName("다양한 상태의 챌린지 중 완료된 것만 조회된다.")
    @Test
    void getFinishedChallengeListWithVariousStatuses() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        User savedUser = userRepository.save(user);

        // SecurityService Mock 설정
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(LoginUserInfo.of(savedUser.getId()));

        Challenge challenge1 = createChallenge("완료 챌린지1", "내용1", "badge1.png", 100);
        Challenge challenge2 = createChallenge("진행중 챌린지", "내용2", "badge2.png", 200);
        Challenge challenge3 = createChallenge("완료 챌린지2", "내용3", "badge3.png", 300);
        Challenge challenge4 = createChallenge("실패 챌린지", "내용4", "badge4.png", 400);
        Challenge challenge5 = createChallenge("포기 챌린지", "내용5", "badge5.png", 500);

        Challenge savedChallenge1 = cChallengeJpaRepository.save(challenge1);
        Challenge savedChallenge2 = cChallengeJpaRepository.save(challenge2);
        Challenge savedChallenge3 = cChallengeJpaRepository.save(challenge3);
        Challenge savedChallenge4 = cChallengeJpaRepository.save(challenge4);
        Challenge savedChallenge5 = cChallengeJpaRepository.save(challenge5);

        // 다양한 상태의 챌린지 이력 생성
        ChallengeHistory history1 = createChallengeHistory(savedChallenge1, savedUser, ChallengeStatus.COMPLETED);
        ChallengeHistory history2 = createChallengeHistory(savedChallenge2, savedUser, ChallengeStatus.PROGRESSING);
        ChallengeHistory history3 = createChallengeHistory(savedChallenge3, savedUser, ChallengeStatus.COMPLETED);
        ChallengeHistory history4 = createChallengeHistory(savedChallenge4, savedUser, ChallengeStatus.FAILED);
        ChallengeHistory history5 = createChallengeHistory(savedChallenge5, savedUser, ChallengeStatus.CANCELLED);

        ChallengeHistory savedHistory1 = challengeHistoryJpaRepository.save(history1);
        challengeHistoryJpaRepository.save(history2);
        ChallengeHistory savedHistory3 = challengeHistoryJpaRepository.save(history3);
        challengeHistoryJpaRepository.save(history4);
        challengeHistoryJpaRepository.save(history5);

        PageInfoServiceRequest pageRequest = PageInfoServiceRequest.builder()
                .page(1)
                .size(10)
                .build();

        // when
        PageCustom<ChallengeHistoryResponse> response = challengeHistoryReadService.getFinishedChallengeList(pageRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(2); // COMPLETED 상태만 2개
        assertThat(response.getPageInfo().getTotalElement()).isEqualTo(2);

        // ChallengeHistoryResponse 모든 필드(6개) 검증 - 완료된 챌린지만
        assertThat(response.getContent())
                .extracting("challengeHistoryId", "title", "content", "badge", "reward", "status")
                .containsExactlyInAnyOrder(
                        tuple(savedHistory1.getId(), "완료 챌린지1", "내용1", "badge1.png", 100, ChallengeStatus.COMPLETED),
                        tuple(savedHistory3.getId(), "완료 챌린지2", "내용3", "badge3.png", 300, ChallengeStatus.COMPLETED)
                );
    }
}
