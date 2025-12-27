package com.addiction.challenge.missionhistory.service;

import com.addiction.IntegrationTestSupport;
import com.addiction.challenge.challange.entity.Challenge;
import com.addiction.challenge.challengehistory.entity.ChallengeHistory;
import com.addiction.challenge.mission.entity.Mission;
import com.addiction.challenge.missionhistory.entity.MissionHistory;
import com.addiction.challenge.missionhistory.service.response.MissionProgressResponse;
import com.addiction.common.enums.ChallengeStatus;
import com.addiction.common.enums.MissionCategoryStatus;
import com.addiction.common.enums.MissionStatus;
import com.addiction.global.exception.AddictionException;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MissionHistoryReadServiceTest extends IntegrationTestSupport {

    @Autowired
    private MissionHistoryReadService missionHistoryReadService;

    @DisplayName("챌린지 이력 ID로 미션 진행 상황을 조회한다.")
    @Test
    void getMissionProgress() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        User savedUser = userRepository.save(user);

        Challenge challenge = createChallenge("유혹 피하기", "흡연 유혹 상황을 피하는 챌린지", "badge_url", 500);
        Challenge savedChallenge = cChallengeJpaRepository.save(challenge);

        ChallengeHistory challengeHistory = createChallengeHistory(savedChallenge, savedUser, ChallengeStatus.PROGRESSING);
        ChallengeHistory savedChallengeHistory = challengeHistoryJpaRepository.save(challengeHistory);

        Mission mission1 = createMission(savedChallenge, MissionCategoryStatus.LOCATION, 
                "평소 흡연 루트 피해보기", "평소 다니는 흡연 루트를 피해서 이동하기", 50);
        Mission mission2 = createMission(savedChallenge, MissionCategoryStatus.REPLACE_ACTION, 
                "평소 흡연 시간대 산책하기", "평소 흡연하는 시간대에 산책해보기", 70);
        Mission mission3 = createMission(savedChallenge, MissionCategoryStatus.HOLD, 
                "5분간 참기", "흡연 욕구가 생길 때 5분간 참아보기", 30);

        Mission savedMission1 = missionJpaRepository.save(mission1);
        Mission savedMission2 = missionJpaRepository.save(mission2);
        Mission savedMission3 = missionJpaRepository.save(mission3);

        MissionHistory missionHistory1 = createMissionHistory(savedChallengeHistory, savedMission1, savedUser, 
                MissionStatus.COMPLETED, "서울특별시 강남구");
        MissionHistory missionHistory2 = createMissionHistory(savedChallengeHistory, savedMission2, savedUser, 
                MissionStatus.COMPLETED, "서울특별시 서초구");
        MissionHistory missionHistory3 = createMissionHistory(savedChallengeHistory, savedMission3, savedUser, 
                MissionStatus.PROGRESSING, null);

        MissionHistory savedMissionHistory1 = missionHistoryJpaRepository.save(missionHistory1);
        MissionHistory savedMissionHistory2 = missionHistoryJpaRepository.save(missionHistory2);
        MissionHistory savedMissionHistory3 = missionHistoryJpaRepository.save(missionHistory3);

        // when
        MissionProgressResponse response = missionHistoryReadService.getMissionProgress(savedChallengeHistory.getId());

        // then
        // MissionProgressResponse 모든 필드(8개) 검증
        assertThat(response).isNotNull();
        assertThat(response.getChallengeHistoryId()).isEqualTo(savedChallengeHistory.getId());
        assertThat(response.getChallengeId()).isEqualTo(savedChallenge.getId());
        assertThat(response.getChallengeTitle()).isEqualTo("유혹 피하기");
        assertThat(response.getTotalMissionCount()).isEqualTo(3);
        assertThat(response.getCompletedMissionCount()).isEqualTo(2);
        assertThat(response.getTotalEarnedReward()).isEqualTo(120); // 50 + 70
        assertThat(response.getTotalPossibleReward()).isEqualTo(150); // 50 + 70 + 30
        assertThat(response.getMissions()).isNotNull().hasSize(3);

        // MissionHistoryResponse 모든 필드(7개) 검증
        assertThat(response.getMissions())
                .extracting("missionHistoryId", "missionId", "missionTitle", "missionContent", "category", "reward", "status")
                .containsExactlyInAnyOrder(
                        tuple(savedMissionHistory1.getId(), savedMission1.getId(), "평소 흡연 루트 피해보기",
                                "평소 다니는 흡연 루트를 피해서 이동하기", MissionCategoryStatus.LOCATION, 50, MissionStatus.COMPLETED),
                        tuple(savedMissionHistory2.getId(), savedMission2.getId(), "평소 흡연 시간대 산책하기",
                                "평소 흡연하는 시간대에 산책해보기", MissionCategoryStatus.REPLACE_ACTION, 70, MissionStatus.COMPLETED),
                        tuple(savedMissionHistory3.getId(), savedMission3.getId(), "5분간 참기",
                                "흡연 욕구가 생길 때 5분간 참아보기", MissionCategoryStatus.HOLD, 30, MissionStatus.PROGRESSING)
                );
    }

    @DisplayName("모든 미션이 완료된 경우 진행 상황을 조회한다.")
    @Test
    void getMissionProgressWithAllCompleted() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        User savedUser = userRepository.save(user);

        Challenge challenge = createChallenge("완료 챌린지", "모두 완료", "badge_url", 300);
        Challenge savedChallenge = cChallengeJpaRepository.save(challenge);

        ChallengeHistory challengeHistory = createChallengeHistory(savedChallenge, savedUser, ChallengeStatus.COMPLETED);
        ChallengeHistory savedChallengeHistory = challengeHistoryJpaRepository.save(challengeHistory);

        Mission mission1 = createMission(savedChallenge, MissionCategoryStatus.LOCATION, "미션1", "내용1", 100);
        Mission mission2 = createMission(savedChallenge, MissionCategoryStatus.HOLD, "미션2", "내용2", 150);

        Mission savedMission1 = missionJpaRepository.save(mission1);
        Mission savedMission2 = missionJpaRepository.save(mission2);

        MissionHistory missionHistory1 = createMissionHistory(savedChallengeHistory, savedMission1, savedUser, 
                MissionStatus.COMPLETED, "서울특별시");
        MissionHistory missionHistory2 = createMissionHistory(savedChallengeHistory, savedMission2, savedUser, 
                MissionStatus.COMPLETED, "경기도");

        missionHistoryJpaRepository.saveAll(List.of(missionHistory1, missionHistory2));

        // when
        MissionProgressResponse response = missionHistoryReadService.getMissionProgress(savedChallengeHistory.getId());

        // then
        // MissionProgressResponse 모든 필드(8개) 검증
        assertThat(response).isNotNull();
        assertThat(response.getChallengeHistoryId()).isEqualTo(savedChallengeHistory.getId());
        assertThat(response.getChallengeId()).isEqualTo(savedChallenge.getId());
        assertThat(response.getChallengeTitle()).isEqualTo("완료 챌린지");
        assertThat(response.getTotalMissionCount()).isEqualTo(2);
        assertThat(response.getCompletedMissionCount()).isEqualTo(2);
        assertThat(response.getTotalEarnedReward()).isEqualTo(250);
        assertThat(response.getTotalPossibleReward()).isEqualTo(250);
        assertThat(response.getMissions()).isNotNull().hasSize(2);
    }

    @DisplayName("모든 미션이 진행중인 경우 진행 상황을 조회한다.")
    @Test
    void getMissionProgressWithNoneCompleted() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        User savedUser = userRepository.save(user);

        Challenge challenge = createChallenge("시작 챌린지", "막 시작", "badge_url", 200);
        Challenge savedChallenge = cChallengeJpaRepository.save(challenge);

        ChallengeHistory challengeHistory = createChallengeHistory(savedChallenge, savedUser, ChallengeStatus.PROGRESSING);
        ChallengeHistory savedChallengeHistory = challengeHistoryJpaRepository.save(challengeHistory);

        Mission mission1 = createMission(savedChallenge, MissionCategoryStatus.LOCATION, "미션1", "내용1", 80);
        Mission mission2 = createMission(savedChallenge, MissionCategoryStatus.REPLACE_ACTION, "미션2", "내용2", 90);

        Mission savedMission1 = missionJpaRepository.save(mission1);
        Mission savedMission2 = missionJpaRepository.save(mission2);

        MissionHistory missionHistory1 = createMissionHistory(savedChallengeHistory, savedMission1, savedUser, 
                MissionStatus.PROGRESSING, null);
        MissionHistory missionHistory2 = createMissionHistory(savedChallengeHistory, savedMission2, savedUser, 
                MissionStatus.PROGRESSING, null);

        missionHistoryJpaRepository.saveAll(List.of(missionHistory1, missionHistory2));

        // when
        MissionProgressResponse response = missionHistoryReadService.getMissionProgress(savedChallengeHistory.getId());

        // then
        // MissionProgressResponse 모든 필드(8개) 검증
        assertThat(response).isNotNull();
        assertThat(response.getChallengeHistoryId()).isEqualTo(savedChallengeHistory.getId());
        assertThat(response.getChallengeId()).isEqualTo(savedChallenge.getId());
        assertThat(response.getChallengeTitle()).isEqualTo("시작 챌린지");
        assertThat(response.getTotalMissionCount()).isEqualTo(2);
        assertThat(response.getCompletedMissionCount()).isEqualTo(0);
        assertThat(response.getTotalEarnedReward()).isEqualTo(0);
        assertThat(response.getTotalPossibleReward()).isEqualTo(170);
        assertThat(response.getMissions()).isNotNull().hasSize(2);
    }

    @DisplayName("미션 이력이 하나도 없는 경우 빈 목록을 반환한다.")
    @Test
    void getMissionProgressWithNoMissionHistory() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        User savedUser = userRepository.save(user);

        Challenge challenge = createChallenge("빈 챌린지", "미션 이력 없음", "badge_url", 0);
        Challenge savedChallenge = cChallengeJpaRepository.save(challenge);

        ChallengeHistory challengeHistory = createChallengeHistory(savedChallenge, savedUser, ChallengeStatus.PROGRESSING);
        ChallengeHistory savedChallengeHistory = challengeHistoryJpaRepository.save(challengeHistory);

        // when
        MissionProgressResponse response = missionHistoryReadService.getMissionProgress(savedChallengeHistory.getId());

        // then
        // MissionProgressResponse 모든 필드(8개) 검증
        assertThat(response).isNotNull();
        assertThat(response.getChallengeHistoryId()).isEqualTo(savedChallengeHistory.getId());
        assertThat(response.getChallengeId()).isEqualTo(savedChallenge.getId());
        assertThat(response.getChallengeTitle()).isEqualTo("빈 챌린지");
        assertThat(response.getTotalMissionCount()).isEqualTo(0);
        assertThat(response.getCompletedMissionCount()).isEqualTo(0);
        assertThat(response.getTotalEarnedReward()).isEqualTo(0);
        assertThat(response.getTotalPossibleReward()).isEqualTo(0);
        assertThat(response.getMissions()).isNotNull().isEmpty();
    }

    @DisplayName("존재하지 않는 챌린지 이력 ID로 조회시 예외가 발생한다.")
    @Test
    void getMissionProgressWithNonExistentChallengeHistoryId() {
        // given
        Long nonExistentChallengeHistoryId = 999L;

        // when // then
        assertThatThrownBy(() -> missionHistoryReadService.getMissionProgress(nonExistentChallengeHistoryId))
                .isInstanceOf(AddictionException.class)
                .hasMessage("챌린지 이력을 찾을 수 없습니다.");
    }

    @DisplayName("일부 미션만 완료된 경우 완료율이 정확히 계산된다.")
    @Test
    void getMissionProgressWithPartialCompletion() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        User savedUser = userRepository.save(user);

        Challenge challenge = createChallenge("부분 완료 챌린지", "일부만 완료", "badge_url", 400);
        Challenge savedChallenge = cChallengeJpaRepository.save(challenge);

        ChallengeHistory challengeHistory = createChallengeHistory(savedChallenge, savedUser, ChallengeStatus.PROGRESSING);
        ChallengeHistory savedChallengeHistory = challengeHistoryJpaRepository.save(challengeHistory);

        Mission mission1 = createMission(savedChallenge, MissionCategoryStatus.LOCATION, "미션1", "내용1", 100);
        Mission mission2 = createMission(savedChallenge, MissionCategoryStatus.REPLACE_ACTION, "미션2", "내용2", 100);
        Mission mission3 = createMission(savedChallenge, MissionCategoryStatus.HOLD, "미션3", "내용3", 100);
        Mission mission4 = createMission(savedChallenge, MissionCategoryStatus.LOCATION, "미션4", "내용4", 100);

        Mission savedMission1 = missionJpaRepository.save(mission1);
        Mission savedMission2 = missionJpaRepository.save(mission2);
        Mission savedMission3 = missionJpaRepository.save(mission3);
        Mission savedMission4 = missionJpaRepository.save(mission4);

        MissionHistory missionHistory1 = createMissionHistory(savedChallengeHistory, savedMission1, savedUser, 
                MissionStatus.COMPLETED, "주소1");
        MissionHistory missionHistory2 = createMissionHistory(savedChallengeHistory, savedMission2, savedUser, 
                MissionStatus.PROGRESSING, null);
        MissionHistory missionHistory3 = createMissionHistory(savedChallengeHistory, savedMission3, savedUser, 
                MissionStatus.COMPLETED, "주소3");
        MissionHistory missionHistory4 = createMissionHistory(savedChallengeHistory, savedMission4, savedUser, 
                MissionStatus.PROGRESSING, null);

        missionHistoryJpaRepository.saveAll(List.of(missionHistory1, missionHistory2, missionHistory3, missionHistory4));

        // when
        MissionProgressResponse response = missionHistoryReadService.getMissionProgress(savedChallengeHistory.getId());

        // then
        // MissionProgressResponse 모든 필드(8개) 검증
        assertThat(response).isNotNull();
        assertThat(response.getChallengeHistoryId()).isEqualTo(savedChallengeHistory.getId());
        assertThat(response.getChallengeId()).isEqualTo(savedChallenge.getId());
        assertThat(response.getChallengeTitle()).isEqualTo("부분 완료 챌린지");
        assertThat(response.getTotalMissionCount()).isEqualTo(4);
        assertThat(response.getCompletedMissionCount()).isEqualTo(2);
        assertThat(response.getTotalEarnedReward()).isEqualTo(200); // 100 + 100
        assertThat(response.getTotalPossibleReward()).isEqualTo(400); // 100 * 4
        assertThat(response.getMissions()).isNotNull().hasSize(4);
    }

    @DisplayName("단일 미션 이력의 모든 필드가 정확히 매핑된다.")
    @Test
    void getSingleMissionHistoryWithAllFields() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);
        User savedUser = userRepository.save(user);

        Challenge challenge = createChallenge("단일 미션 챌린지", "단일 미션", "badge_url", 100);
        Challenge savedChallenge = cChallengeJpaRepository.save(challenge);

        ChallengeHistory challengeHistory = createChallengeHistory(savedChallenge, savedUser, ChallengeStatus.PROGRESSING);
        ChallengeHistory savedChallengeHistory = challengeHistoryJpaRepository.save(challengeHistory);

        Mission mission = createMission(savedChallenge, MissionCategoryStatus.REPLACE_ACTION, 
                "테스트 미션", "테스트 미션 내용", 85);
        Mission savedMission = missionJpaRepository.save(mission);

        MissionHistory missionHistory = createMissionHistory(savedChallengeHistory, savedMission, savedUser, 
                MissionStatus.COMPLETED, "서울특별시 강남구 역삼동");
        MissionHistory savedMissionHistory = missionHistoryJpaRepository.save(missionHistory);

        // when
        MissionProgressResponse response = missionHistoryReadService.getMissionProgress(savedChallengeHistory.getId());

        // then
        // MissionProgressResponse 모든 필드(8개) 검증
        assertThat(response.getChallengeHistoryId()).isEqualTo(savedChallengeHistory.getId());
        assertThat(response.getChallengeId()).isEqualTo(savedChallenge.getId());
        assertThat(response.getChallengeTitle()).isEqualTo("단일 미션 챌린지");
        assertThat(response.getTotalMissionCount()).isEqualTo(1);
        assertThat(response.getCompletedMissionCount()).isEqualTo(1);
        assertThat(response.getTotalEarnedReward()).isEqualTo(85);
        assertThat(response.getTotalPossibleReward()).isEqualTo(85);
        assertThat(response.getMissions()).hasSize(1);

        // MissionHistoryResponse 모든 필드(7개) 검증
        assertThat(response.getMissions())
                .extracting("missionHistoryId", "missionId", "missionTitle", "missionContent", "category", "reward", "status")
                .containsExactly(
                        tuple(savedMissionHistory.getId(), savedMission.getId(), "테스트 미션", 
                              "테스트 미션 내용", MissionCategoryStatus.REPLACE_ACTION, 85, MissionStatus.COMPLETED)
                );
    }
}
