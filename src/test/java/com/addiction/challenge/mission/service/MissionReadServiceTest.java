package com.addiction.challenge.mission.service;

import com.addiction.IntegrationTestSupport;
import com.addiction.challenge.challange.entity.Challenge;
import com.addiction.challenge.mission.entity.Mission;
import com.addiction.challenge.mission.service.response.MissionListResponse;
import com.addiction.challenge.mission.service.response.MissionResponse;
import com.addiction.common.enums.MissionCategoryStatus;
import com.addiction.global.exception.AddictionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MissionReadServiceTest extends IntegrationTestSupport {

    @Autowired
    private MissionReadService missionReadService;

    @DisplayName("챌린지 ID로 미션 목록을 조회한다.")
    @Test
    void getMissionListByChallengeId() {
        // given
        Challenge challenge = createChallenge("유혹 피하기", "흡연 유혹 상황을 피하는 챌린지", "badge_url", 500);
        Challenge savedChallenge = cChallengeJpaRepository.save(challenge);

        Mission mission1 = createMission(savedChallenge, MissionCategoryStatus.LOCATION, 
                "평소 흡연 루트 피해보기", "평소 다니는 흡연 루트를 피해서 이동하기", 50);
        Mission mission2 = createMission(savedChallenge, MissionCategoryStatus.REPLACE_ACTION, 
                "평소 흡연 시간대 산책하기", "평소 흡연하는 시간대에 산책해보기", 70);
        Mission mission3 = createMission(savedChallenge, MissionCategoryStatus.HOLD, 
                "5분간 참기", "흡연 욕구가 생길 때 5분간 참아보기", 30);
        
        Mission savedMission1 = missionJpaRepository.save(mission1);
        Mission savedMission2 = missionJpaRepository.save(mission2);
        Mission savedMission3 = missionJpaRepository.save(mission3);

        // when
        MissionListResponse response = missionReadService.getMissionListByChallengeId(savedChallenge.getId());

        // then
        // MissionListResponse의 모든 필드(5개) 검증
        assertThat(response).isNotNull();
        assertThat(response.getChallengeId()).isEqualTo(savedChallenge.getId());
        assertThat(response.getChallengeTitle()).isEqualTo("유혹 피하기");
        assertThat(response.getTotalMissionCount()).isEqualTo(3);
        assertThat(response.getTotalReward()).isEqualTo(150);
        assertThat(response.getMissions()).isNotNull().hasSize(3);
        
        // 각 MissionResponse의 모든 필드(6개) 검증: missionId, title, content, reward, category, status
        assertThat(response.getMissions())
                .extracting("missionId", "title", "content", "reward", "category", "status")
                .containsExactlyInAnyOrder(
                        tuple(savedMission1.getId(), "평소 흡연 루트 피해보기", 
                              "평소 다니는 흡연 루트를 피해서 이동하기", 50, MissionCategoryStatus.LOCATION, null),
                        tuple(savedMission2.getId(), "평소 흡연 시간대 산책하기", 
                              "평소 흡연하는 시간대에 산책해보기", 70, MissionCategoryStatus.REPLACE_ACTION, null),
                        tuple(savedMission3.getId(), "5분간 참기", 
                              "흡연 욕구가 생길 때 5분간 참아보기", 30, MissionCategoryStatus.HOLD, null)
                );
    }

    @DisplayName("챌린지에 미션이 하나도 없을 때 빈 목록을 반환한다.")
    @Test
    void getMissionListByChallengeIdWithNoMissions() {
        // given
        Challenge challenge = createChallenge("빈 챌린지", "미션이 없는 챌린지", "badge_url", 0);
        Challenge savedChallenge = cChallengeJpaRepository.save(challenge);

        // when
        MissionListResponse response = missionReadService.getMissionListByChallengeId(savedChallenge.getId());

        // then
        // MissionListResponse의 모든 필드(5개) 검증
        assertThat(response).isNotNull();
        assertThat(response.getChallengeId()).isEqualTo(savedChallenge.getId());
        assertThat(response.getChallengeTitle()).isEqualTo("빈 챌린지");
        assertThat(response.getTotalMissionCount()).isEqualTo(0);
        assertThat(response.getTotalReward()).isEqualTo(0);
        assertThat(response.getMissions()).isNotNull().isEmpty();
    }

    @DisplayName("존재하지 않는 챌린지 ID로 조회시 예외가 발생한다.")
    @Test
    void getMissionListByNonExistentChallengeId() {
        // given
        Long nonExistentChallengeId = 999L;

        // when // then
        assertThatThrownBy(() -> missionReadService.getMissionListByChallengeId(nonExistentChallengeId))
                .isInstanceOf(AddictionException.class)
                .hasMessage("챌린지를 찾을 수 없습니다.");
    }

    @DisplayName("챌린지에 다양한 카테고리의 미션들이 있을 때 모두 조회된다.")
    @Test
    void getMissionListWithVariousCategories() {
        // given
        Challenge challenge = createChallenge("종합 챌린지", "다양한 미션들", "badge_url", 1000);
        Challenge savedChallenge = cChallengeJpaRepository.save(challenge);

        Mission mission1 = createMission(savedChallenge, MissionCategoryStatus.LOCATION, 
                "위치 미션1", "위치 기반 미션1", 100);
        Mission mission2 = createMission(savedChallenge, MissionCategoryStatus.REPLACE_ACTION, 
                "대체 행동 미션1", "대체 행동 미션1", 150);
        Mission mission3 = createMission(savedChallenge, MissionCategoryStatus.HOLD, 
                "참기 미션1", "참기 미션1", 80);
        Mission mission4 = createMission(savedChallenge, MissionCategoryStatus.LOCATION, 
                "위치 미션2", "위치 기반 미션2", 120);
        Mission mission5 = createMission(savedChallenge, MissionCategoryStatus.REPLACE_ACTION, 
                "대체 행동 미션2", "대체 행동 미션2", 90);
        
        Mission savedMission1 = missionJpaRepository.save(mission1);
        Mission savedMission2 = missionJpaRepository.save(mission2);
        Mission savedMission3 = missionJpaRepository.save(mission3);
        Mission savedMission4 = missionJpaRepository.save(mission4);
        Mission savedMission5 = missionJpaRepository.save(mission5);

        // when
        MissionListResponse response = missionReadService.getMissionListByChallengeId(savedChallenge.getId());

        // then
        // MissionListResponse의 모든 필드(5개) 검증
        assertThat(response).isNotNull();
        assertThat(response.getChallengeId()).isEqualTo(savedChallenge.getId());
        assertThat(response.getChallengeTitle()).isEqualTo("종합 챌린지");
        assertThat(response.getTotalMissionCount()).isEqualTo(5);
        assertThat(response.getTotalReward()).isEqualTo(540);
        assertThat(response.getMissions()).isNotNull().hasSize(5);
        
        // 각 MissionResponse의 모든 필드(6개) 검증: missionId, title, content, reward, category, status
        assertThat(response.getMissions())
                .extracting("missionId", "title", "content", "reward", "category", "status")
                .containsExactlyInAnyOrder(
                        tuple(savedMission1.getId(), "위치 미션1", "위치 기반 미션1", 
                              100, MissionCategoryStatus.LOCATION, null),
                        tuple(savedMission2.getId(), "대체 행동 미션1", "대체 행동 미션1", 
                              150, MissionCategoryStatus.REPLACE_ACTION, null),
                        tuple(savedMission3.getId(), "참기 미션1", "참기 미션1", 
                              80, MissionCategoryStatus.HOLD, null),
                        tuple(savedMission4.getId(), "위치 미션2", "위치 기반 미션2", 
                              120, MissionCategoryStatus.LOCATION, null),
                        tuple(savedMission5.getId(), "대체 행동 미션2", "대체 행동 미션2", 
                              90, MissionCategoryStatus.REPLACE_ACTION, null)
                );
    }

    @DisplayName("미션의 상세 정보가 모두 포함되어 조회된다.")
    @Test
    void getMissionListWithFullDetails() {
        // given
        Challenge challenge = createChallenge("상세 챌린지", "상세 정보 확인용", "badge_url", 300);
        Challenge savedChallenge = cChallengeJpaRepository.save(challenge);

        Mission mission = createMission(
                savedChallenge, 
                MissionCategoryStatus.LOCATION, 
                "상세 미션 제목", 
                "상세 미션 내용입니다. 이것은 미션에 대한 자세한 설명입니다.", 
                200
        );
        Mission savedMission = missionJpaRepository.save(mission);

        // when
        MissionListResponse response = missionReadService.getMissionListByChallengeId(savedChallenge.getId());

        // then
        // MissionListResponse의 모든 필드(5개) 검증
        assertThat(response).isNotNull();
        assertThat(response.getChallengeId()).isEqualTo(savedChallenge.getId());
        assertThat(response.getChallengeTitle()).isEqualTo("상세 챌린지");
        assertThat(response.getTotalMissionCount()).isEqualTo(1);
        assertThat(response.getTotalReward()).isEqualTo(200);
        assertThat(response.getMissions()).isNotNull().hasSize(1);
        
        // MissionResponse의 모든 필드(6개) 개별 검증: missionId, title, content, reward, category, status
        MissionResponse missionResponse = response.getMissions().get(0);
        assertThat(missionResponse.getMissionId()).isEqualTo(savedMission.getId());
        assertThat(missionResponse.getTitle()).isEqualTo("상세 미션 제목");
        assertThat(missionResponse.getContent()).isEqualTo("상세 미션 내용입니다. 이것은 미션에 대한 자세한 설명입니다.");
        assertThat(missionResponse.getReward()).isEqualTo(200);
        assertThat(missionResponse.getCategory()).isEqualTo(MissionCategoryStatus.LOCATION);
        assertThat(missionResponse.getStatus()).isNull();
    }

    @DisplayName("여러 미션의 보상 포인트가 정확히 합산된다.")
    @Test
    void calculateTotalRewardCorrectly() {
        // given
        Challenge challenge = createChallenge("보상 테스트 챌린지", "보상 합산 테스트", "badge_url", 1000);
        Challenge savedChallenge = cChallengeJpaRepository.save(challenge);

        Mission mission1 = createMission(savedChallenge, MissionCategoryStatus.LOCATION, 
                "미션1", "내용1", 100);
        Mission mission2 = createMission(savedChallenge, MissionCategoryStatus.REPLACE_ACTION, 
                "미션2", "내용2", 250);
        Mission mission3 = createMission(savedChallenge, MissionCategoryStatus.HOLD, 
                "미션3", "내용3", 175);
        
        Mission savedMission1 = missionJpaRepository.save(mission1);
        Mission savedMission2 = missionJpaRepository.save(mission2);
        Mission savedMission3 = missionJpaRepository.save(mission3);

        // when
        MissionListResponse response = missionReadService.getMissionListByChallengeId(savedChallenge.getId());

        // then
        // MissionListResponse의 모든 필드(5개) 검증
        assertThat(response).isNotNull();
        assertThat(response.getChallengeId()).isEqualTo(savedChallenge.getId());
        assertThat(response.getChallengeTitle()).isEqualTo("보상 테스트 챌린지");
        assertThat(response.getTotalMissionCount()).isEqualTo(3);
        assertThat(response.getTotalReward()).isEqualTo(525); // 100 + 250 + 175
        assertThat(response.getMissions()).isNotNull().hasSize(3);
        
        // 각 MissionResponse의 모든 필드(6개) 검증
        assertThat(response.getMissions())
                .extracting("missionId", "title", "content", "reward", "category", "status")
                .containsExactlyInAnyOrder(
                        tuple(savedMission1.getId(), "미션1", "내용1", 100, MissionCategoryStatus.LOCATION, null),
                        tuple(savedMission2.getId(), "미션2", "내용2", 250, MissionCategoryStatus.REPLACE_ACTION, null),
                        tuple(savedMission3.getId(), "미션3", "내용3", 175, MissionCategoryStatus.HOLD, null)
                );
    }

    @DisplayName("단일 미션 조회시 모든 필드가 정확히 매핑된다.")
    @Test
    void getSingleMissionWithAllFields() {
        // given
        Challenge challenge = createChallenge("단일 미션 챌린지", "단일 미션 테스트", "badge_test", 100);
        Challenge savedChallenge = cChallengeJpaRepository.save(challenge);

        Mission mission = createMission(savedChallenge, MissionCategoryStatus.REPLACE_ACTION, 
                "테스트 미션", "테스트 미션 상세 내용", 85);
        Mission savedMission = missionJpaRepository.save(mission);

        // when
        MissionListResponse response = missionReadService.getMissionListByChallengeId(savedChallenge.getId());

        // then
        // MissionListResponse의 모든 필드(5개) 검증
        assertThat(response.getChallengeId()).isEqualTo(savedChallenge.getId());
        assertThat(response.getChallengeTitle()).isEqualTo("단일 미션 챌린지");
        assertThat(response.getTotalMissionCount()).isEqualTo(1);
        assertThat(response.getTotalReward()).isEqualTo(85);
        assertThat(response.getMissions()).hasSize(1);

        // MissionResponse의 모든 필드(6개) 개별 검증
        MissionResponse missionResponse = response.getMissions().get(0);
        assertThat(missionResponse.getMissionId()).isEqualTo(savedMission.getId());
        assertThat(missionResponse.getTitle()).isEqualTo("테스트 미션");
        assertThat(missionResponse.getContent()).isEqualTo("테스트 미션 상세 내용");
        assertThat(missionResponse.getReward()).isEqualTo(85);
        assertThat(missionResponse.getCategory()).isEqualTo(MissionCategoryStatus.REPLACE_ACTION);
        assertThat(missionResponse.getStatus()).isNull();
    }
}
