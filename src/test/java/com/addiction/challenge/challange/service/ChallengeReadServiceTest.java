package com.addiction.challenge.challange.service;

import com.addiction.IntegrationTestSupport;
import com.addiction.challenge.challange.entity.Challenge;
import com.addiction.challenge.challange.service.response.ChallengeResponse;
import com.addiction.challenge.challengehistory.entity.ChallengeHistory;
import com.addiction.common.enums.ChallengeStatus;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class ChallengeReadServiceTest extends IntegrationTestSupport {

    @Autowired
    private ChallengeReadService challengeReadService;

    @DisplayName("남은 챌린지 목록을 페이징하여 조회한다.")
    @Test
    void getLeftChallengeList() {
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
        Challenge challenge5 = createChallenge("챌린지5", "내용5", "badge5.png", 500);

        Challenge savedChallenge1 = cChallengeJpaRepository.save(challenge1);
        Challenge savedChallenge2 = cChallengeJpaRepository.save(challenge2);
        Challenge savedChallenge3 = cChallengeJpaRepository.save(challenge3);
        Challenge savedChallenge4 = cChallengeJpaRepository.save(challenge4);
        Challenge savedChallenge5 = cChallengeJpaRepository.save(challenge5);

        // 챌린지1, 2는 이미 시작함
        ChallengeHistory history1 = createChallengeHistory(savedChallenge1, savedUser, ChallengeStatus.PROGRESSING);
        ChallengeHistory history2 = createChallengeHistory(savedChallenge2, savedUser, ChallengeStatus.COMPLETED);
        challengeHistoryJpaRepository.saveAll(List.of(history1, history2));

        PageInfoServiceRequest pageRequest = PageInfoServiceRequest.builder()
                .page(1)
                .size(10)
                .build();

        // when
        PageCustom<ChallengeResponse> response = challengeReadService.getLeftChallengeList(pageRequest);

        // then
        // PageCustom 검증
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(3); // 5개 중 2개는 이미 시작했으므로 3개만 남음
        assertThat(response.getPageInfo()).isNotNull();
        
        // PageableCustom 모든 필드(3개) 검증
        assertThat(response.getPageInfo().getCurrentPage()).isEqualTo(1);
        assertThat(response.getPageInfo().getTotalPage()).isEqualTo(1);
        assertThat(response.getPageInfo().getTotalElement()).isEqualTo(3);

        // ChallengeResponse 모든 필드(5개) 검증
        assertThat(response.getContent())
                .extracting("challengeId", "title", "content", "badge", "reward")
                .containsExactlyInAnyOrder(
                        tuple(savedChallenge3.getId(), "챌린지3", "내용3", "badge3.png", 300),
                        tuple(savedChallenge4.getId(), "챌린지4", "내용4", "badge4.png", 400),
                        tuple(savedChallenge5.getId(), "챌린지5", "내용5", "badge5.png", 500)
                );
    }

    @DisplayName("모든 챌린지를 시작한 경우 빈 목록을 반환한다.")
    @Test
    void getLeftChallengeListWhenAllStarted() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        User savedUser = userRepository.save(user);

        // SecurityService Mock 설정
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(LoginUserInfo.of(savedUser.getId()));

        Challenge challenge1 = createChallenge("챌린지1", "내용1", "badge1.png", 100);
        Challenge challenge2 = createChallenge("챌린지2", "내용2", "badge2.png", 200);

        Challenge savedChallenge1 = cChallengeJpaRepository.save(challenge1);
        Challenge savedChallenge2 = cChallengeJpaRepository.save(challenge2);

        // 모든 챌린지 시작
        ChallengeHistory history1 = createChallengeHistory(savedChallenge1, savedUser, ChallengeStatus.PROGRESSING);
        ChallengeHistory history2 = createChallengeHistory(savedChallenge2, savedUser, ChallengeStatus.COMPLETED);
        challengeHistoryJpaRepository.saveAll(List.of(history1, history2));

        PageInfoServiceRequest pageRequest = PageInfoServiceRequest.builder()
                .page(1)
                .size(10)
                .build();

        // when
        PageCustom<ChallengeResponse> response = challengeReadService.getLeftChallengeList(pageRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isEmpty();
        assertThat(response.getPageInfo().getCurrentPage()).isEqualTo(1);
        assertThat(response.getPageInfo().getTotalPage()).isEqualTo(0);
        assertThat(response.getPageInfo().getTotalElement()).isEqualTo(0);
    }

    @DisplayName("챌린지가 하나도 없는 경우 빈 목록을 반환한다.")
    @Test
    void getLeftChallengeListWhenNoChallenges() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        User savedUser = userRepository.save(user);

        // SecurityService Mock 설정
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(LoginUserInfo.of(savedUser.getId()));

        PageInfoServiceRequest pageRequest = PageInfoServiceRequest.builder()
                .page(1)
                .size(10)
                .build();

        // when
        PageCustom<ChallengeResponse> response = challengeReadService.getLeftChallengeList(pageRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isEmpty();
        assertThat(response.getPageInfo().getCurrentPage()).isEqualTo(1);
        assertThat(response.getPageInfo().getTotalPage()).isEqualTo(0);
        assertThat(response.getPageInfo().getTotalElement()).isEqualTo(0);
    }

    @DisplayName("페이징이 정상적으로 동작한다 - 2페이지 조회")
    @Test
    void getLeftChallengeListWithPagination() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        User savedUser = userRepository.save(user);

        // SecurityService Mock 설정
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(LoginUserInfo.of(savedUser.getId()));

        // 15개의 챌린지 생성
        for (int i = 1; i <= 15; i++) {
            Challenge challenge = createChallenge("챌린지" + i, "내용" + i, "badge" + i + ".png", i * 100);
            cChallengeJpaRepository.save(challenge);
        }

        PageInfoServiceRequest pageRequest = PageInfoServiceRequest.builder()
                .page(2)
                .size(5)
                .build();

        // when
        PageCustom<ChallengeResponse> response = challengeReadService.getLeftChallengeList(pageRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(5);
        assertThat(response.getPageInfo().getCurrentPage()).isEqualTo(2);
        assertThat(response.getPageInfo().getTotalPage()).isEqualTo(3); // 15개 / 5 = 3페이지
        assertThat(response.getPageInfo().getTotalElement()).isEqualTo(15);
    }

    @DisplayName("페이징 - 마지막 페이지 조회")
    @Test
    void getLeftChallengeListLastPage() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        User savedUser = userRepository.save(user);

        // SecurityService Mock 설정
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(LoginUserInfo.of(savedUser.getId()));

        // 12개의 챌린지 생성
        for (int i = 1; i <= 12; i++) {
            Challenge challenge = createChallenge("챌린지" + i, "내용" + i, "badge" + i + ".png", i * 100);
            cChallengeJpaRepository.save(challenge);
        }

        PageInfoServiceRequest pageRequest = PageInfoServiceRequest.builder()
                .page(3)
                .size(5)
                .build();

        // when
        PageCustom<ChallengeResponse> response = challengeReadService.getLeftChallengeList(pageRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(2); // 마지막 페이지는 2개만
        assertThat(response.getPageInfo().getCurrentPage()).isEqualTo(3);
        assertThat(response.getPageInfo().getTotalPage()).isEqualTo(3);
        assertThat(response.getPageInfo().getTotalElement()).isEqualTo(12);
    }

    @DisplayName("단일 챌린지의 모든 필드가 정확히 매핑된다.")
    @Test
    void getSingleChallengeWithAllFields() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        User savedUser = userRepository.save(user);

        // SecurityService Mock 설정
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(LoginUserInfo.of(savedUser.getId()));

        Challenge challenge = createChallenge("테스트 챌린지", "테스트 챌린지 상세 내용", "test_badge.png", 999);
        Challenge savedChallenge = cChallengeJpaRepository.save(challenge);

        PageInfoServiceRequest pageRequest = PageInfoServiceRequest.builder()
                .page(1)
                .size(10)
                .build();

        // when
        PageCustom<ChallengeResponse> response = challengeReadService.getLeftChallengeList(pageRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);
        
        // ChallengeResponse 모든 필드(5개) 검증
        assertThat(response.getContent())
                .extracting("challengeId", "title", "content", "badge", "reward")
                .containsExactly(
                        tuple(savedChallenge.getId(), "테스트 챌린지", "테스트 챌린지 상세 내용", "test_badge.png", 999)
                );
    }

    @DisplayName("다양한 상태의 챌린지 이력이 있어도 남은 챌린지만 조회된다.")
    @Test
    void getLeftChallengeListWithVariousStatuses() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        User savedUser = userRepository.save(user);

        // SecurityService Mock 설정
        given(securityService.getCurrentLoginUserInfo())
                .willReturn(LoginUserInfo.of(savedUser.getId()));

        Challenge challenge1 = createChallenge("진행중 챌린지", "내용1", "badge1.png", 100);
        Challenge challenge2 = createChallenge("완료 챌린지", "내용2", "badge2.png", 200);
        Challenge challenge3 = createChallenge("실패 챌린지", "내용3", "badge3.png", 300);
        Challenge challenge4 = createChallenge("포기 챌린지", "내용4", "badge4.png", 400);
        Challenge challenge5 = createChallenge("남은 챌린지1", "내용5", "badge5.png", 500);
        Challenge challenge6 = createChallenge("남은 챌린지2", "내용6", "badge6.png", 600);

        Challenge savedChallenge1 = cChallengeJpaRepository.save(challenge1);
        Challenge savedChallenge2 = cChallengeJpaRepository.save(challenge2);
        Challenge savedChallenge3 = cChallengeJpaRepository.save(challenge3);
        Challenge savedChallenge4 = cChallengeJpaRepository.save(challenge4);
        Challenge savedChallenge5 = cChallengeJpaRepository.save(challenge5);
        Challenge savedChallenge6 = cChallengeJpaRepository.save(challenge6);

        // 다양한 상태의 챌린지 이력 생성
        ChallengeHistory history1 = createChallengeHistory(savedChallenge1, savedUser, ChallengeStatus.PROGRESSING);
        ChallengeHistory history2 = createChallengeHistory(savedChallenge2, savedUser, ChallengeStatus.COMPLETED);
        ChallengeHistory history3 = createChallengeHistory(savedChallenge3, savedUser, ChallengeStatus.FAILED);
        ChallengeHistory history4 = createChallengeHistory(savedChallenge4, savedUser, ChallengeStatus.CANCELLED);
        challengeHistoryJpaRepository.saveAll(List.of(history1, history2, history3, history4));

        PageInfoServiceRequest pageRequest = PageInfoServiceRequest.builder()
                .page(1)
                .size(10)
                .build();

        // when
        PageCustom<ChallengeResponse> response = challengeReadService.getLeftChallengeList(pageRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(2);
        assertThat(response.getPageInfo().getTotalElement()).isEqualTo(2);

        // ChallengeResponse 모든 필드(5개) 검증 - 남은 챌린지만
        assertThat(response.getContent())
                .extracting("challengeId", "title", "content", "badge", "reward")
                .containsExactlyInAnyOrder(
                        tuple(savedChallenge5.getId(), "남은 챌린지1", "내용5", "badge5.png", 500),
                        tuple(savedChallenge6.getId(), "남은 챌린지2", "내용6", "badge6.png", 600)
                );
    }
}
