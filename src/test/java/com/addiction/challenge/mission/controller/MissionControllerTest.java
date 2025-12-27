package com.addiction.challenge.mission.controller;

import com.addiction.ControllerTestSupport;
import com.addiction.challenge.mission.service.response.MissionListResponse;
import com.addiction.challenge.mission.service.response.MissionResponse;
import com.addiction.common.enums.MissionCategoryStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MissionControllerTest extends ControllerTestSupport {

    @DisplayName("챌린지 ID로 미션 목록을 조회한다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getMissionListByChallengeId() throws Exception {
        // given
        Long challengeId = 1L;

        List<MissionResponse> missions = List.of(
                MissionResponse.builder()
                        .missionId(1L)
                        .title("평소 흡연 루트 피해보기")
                        .content("평소 다니는 흡연 루트를 피해서 이동하기")
                        .category(MissionCategoryStatus.LOCATION)
                        .reward(50)
                        .status(null)
                        .build(),
                MissionResponse.builder()
                        .missionId(2L)
                        .title("평소 흡연 시간대 산책하기")
                        .content("평소 흡연하는 시간대에 산책해보기")
                        .category(MissionCategoryStatus.REPLACE_ACTION)
                        .reward(70)
                        .status(null)
                        .build(),
                MissionResponse.builder()
                        .missionId(3L)
                        .title("5분간 참기")
                        .content("흡연 욕구가 생길 때 5분간 참아보기")
                        .category(MissionCategoryStatus.HOLD)
                        .reward(30)
                        .status(null)
                        .build()
        );

        MissionListResponse response = MissionListResponse.builder()
                .challengeId(challengeId)
                .challengeTitle("유혹 피하기")
                .totalMissionCount(3)
                .totalReward(150)
                .missions(missions)
                .build();

        given(missionReadService.getMissionListByChallengeId(challengeId))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/mission/challenge/{challengeId}", challengeId)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                // ApiResponse 기본 필드 검증
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                // MissionListResponse 모든 필드(5개) 검증
                .andExpect(jsonPath("$.data.challengeId").value(challengeId))
                .andExpect(jsonPath("$.data.challengeTitle").value("유혹 피하기"))
                .andExpect(jsonPath("$.data.totalMissionCount").value(3))
                .andExpect(jsonPath("$.data.totalReward").value(150))
                .andExpect(jsonPath("$.data.missions").isArray())
                .andExpect(jsonPath("$.data.missions.length()").value(3))
                // 첫 번째 MissionResponse 모든 필드(6개) 검증: missionId, title, content, category, reward, status
                .andExpect(jsonPath("$.data.missions[0].missionId").value(1))
                .andExpect(jsonPath("$.data.missions[0].title").value("평소 흡연 루트 피해보기"))
                .andExpect(jsonPath("$.data.missions[0].content").value("평소 다니는 흡연 루트를 피해서 이동하기"))
                .andExpect(jsonPath("$.data.missions[0].category").value("LOCATION"))
                .andExpect(jsonPath("$.data.missions[0].reward").value(50))
                .andExpect(jsonPath("$.data.missions[0].status").isEmpty())
                // 두 번째 MissionResponse 모든 필드(6개) 검증
                .andExpect(jsonPath("$.data.missions[1].missionId").value(2))
                .andExpect(jsonPath("$.data.missions[1].title").value("평소 흡연 시간대 산책하기"))
                .andExpect(jsonPath("$.data.missions[1].content").value("평소 흡연하는 시간대에 산책해보기"))
                .andExpect(jsonPath("$.data.missions[1].category").value("REPLACE_ACTION"))
                .andExpect(jsonPath("$.data.missions[1].reward").value(70))
                .andExpect(jsonPath("$.data.missions[1].status").isEmpty())
                // 세 번째 MissionResponse 모든 필드(6개) 검증
                .andExpect(jsonPath("$.data.missions[2].missionId").value(3))
                .andExpect(jsonPath("$.data.missions[2].title").value("5분간 참기"))
                .andExpect(jsonPath("$.data.missions[2].content").value("흡연 욕구가 생길 때 5분간 참아보기"))
                .andExpect(jsonPath("$.data.missions[2].category").value("HOLD"))
                .andExpect(jsonPath("$.data.missions[2].reward").value(30))
                .andExpect(jsonPath("$.data.missions[2].status").isEmpty());
    }

    @DisplayName("챌린지에 미션이 없는 경우 빈 목록을 반환한다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getMissionListByChallengeIdWithNoMissions() throws Exception {
        // given
        Long challengeId = 1L;

        MissionListResponse response = MissionListResponse.builder()
                .challengeId(challengeId)
                .challengeTitle("빈 챌린지")
                .totalMissionCount(0)
                .totalReward(0)
                .missions(List.of())
                .build();

        given(missionReadService.getMissionListByChallengeId(challengeId))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/mission/challenge/{challengeId}", challengeId)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                // ApiResponse 기본 필드 검증
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                // MissionListResponse 모든 필드(5개) 검증
                .andExpect(jsonPath("$.data.challengeId").value(challengeId))
                .andExpect(jsonPath("$.data.challengeTitle").value("빈 챌린지"))
                .andExpect(jsonPath("$.data.totalMissionCount").value(0))
                .andExpect(jsonPath("$.data.totalReward").value(0))
                .andExpect(jsonPath("$.data.missions").isEmpty());
    }

    @DisplayName("다양한 카테고리의 미션들을 조회한다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getMissionListWithVariousCategories() throws Exception {
        // given
        Long challengeId = 1L;

        List<MissionResponse> missions = List.of(
                MissionResponse.builder()
                        .missionId(1L)
                        .title("위치 미션1")
                        .content("위치 기반 미션1")
                        .category(MissionCategoryStatus.LOCATION)
                        .reward(100)
                        .status(null)
                        .build(),
                MissionResponse.builder()
                        .missionId(2L)
                        .title("대체 행동 미션1")
                        .content("대체 행동 미션1")
                        .category(MissionCategoryStatus.REPLACE_ACTION)
                        .reward(150)
                        .status(null)
                        .build(),
                MissionResponse.builder()
                        .missionId(3L)
                        .title("참기 미션1")
                        .content("참기 미션1")
                        .category(MissionCategoryStatus.HOLD)
                        .reward(80)
                        .status(null)
                        .build(),
                MissionResponse.builder()
                        .missionId(4L)
                        .title("위치 미션2")
                        .content("위치 기반 미션2")
                        .category(MissionCategoryStatus.LOCATION)
                        .reward(120)
                        .status(null)
                        .build(),
                MissionResponse.builder()
                        .missionId(5L)
                        .title("대체 행동 미션2")
                        .content("대체 행동 미션2")
                        .category(MissionCategoryStatus.REPLACE_ACTION)
                        .reward(90)
                        .status(null)
                        .build()
        );

        MissionListResponse response = MissionListResponse.builder()
                .challengeId(challengeId)
                .challengeTitle("종합 챌린지")
                .totalMissionCount(5)
                .totalReward(540)
                .missions(missions)
                .build();

        given(missionReadService.getMissionListByChallengeId(challengeId))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/mission/challenge/{challengeId}", challengeId)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                // ApiResponse 기본 필드 검증
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                // MissionListResponse 모든 필드(5개) 검증
                .andExpect(jsonPath("$.data.challengeId").value(challengeId))
                .andExpect(jsonPath("$.data.challengeTitle").value("종합 챌린지"))
                .andExpect(jsonPath("$.data.totalMissionCount").value(5))
                .andExpect(jsonPath("$.data.totalReward").value(540))
                .andExpect(jsonPath("$.data.missions").isArray())
                .andExpect(jsonPath("$.data.missions.length()").value(5))
                // 첫 번째 미션 모든 필드(6개) 검증
                .andExpect(jsonPath("$.data.missions[0].missionId").value(1))
                .andExpect(jsonPath("$.data.missions[0].title").value("위치 미션1"))
                .andExpect(jsonPath("$.data.missions[0].content").value("위치 기반 미션1"))
                .andExpect(jsonPath("$.data.missions[0].category").value("LOCATION"))
                .andExpect(jsonPath("$.data.missions[0].reward").value(100))
                .andExpect(jsonPath("$.data.missions[0].status").isEmpty())
                // 두 번째 미션 모든 필드(6개) 검증
                .andExpect(jsonPath("$.data.missions[1].missionId").value(2))
                .andExpect(jsonPath("$.data.missions[1].title").value("대체 행동 미션1"))
                .andExpect(jsonPath("$.data.missions[1].content").value("대체 행동 미션1"))
                .andExpect(jsonPath("$.data.missions[1].category").value("REPLACE_ACTION"))
                .andExpect(jsonPath("$.data.missions[1].reward").value(150))
                .andExpect(jsonPath("$.data.missions[1].status").isEmpty())
                // 세 번째 미션 모든 필드(6개) 검증
                .andExpect(jsonPath("$.data.missions[2].missionId").value(3))
                .andExpect(jsonPath("$.data.missions[2].title").value("참기 미션1"))
                .andExpect(jsonPath("$.data.missions[2].content").value("참기 미션1"))
                .andExpect(jsonPath("$.data.missions[2].category").value("HOLD"))
                .andExpect(jsonPath("$.data.missions[2].reward").value(80))
                .andExpect(jsonPath("$.data.missions[2].status").isEmpty())
                // 네 번째 미션 모든 필드(6개) 검증
                .andExpect(jsonPath("$.data.missions[3].missionId").value(4))
                .andExpect(jsonPath("$.data.missions[3].title").value("위치 미션2"))
                .andExpect(jsonPath("$.data.missions[3].content").value("위치 기반 미션2"))
                .andExpect(jsonPath("$.data.missions[3].category").value("LOCATION"))
                .andExpect(jsonPath("$.data.missions[3].reward").value(120))
                .andExpect(jsonPath("$.data.missions[3].status").isEmpty())
                // 다섯 번째 미션 모든 필드(6개) 검증
                .andExpect(jsonPath("$.data.missions[4].missionId").value(5))
                .andExpect(jsonPath("$.data.missions[4].title").value("대체 행동 미션2"))
                .andExpect(jsonPath("$.data.missions[4].content").value("대체 행동 미션2"))
                .andExpect(jsonPath("$.data.missions[4].category").value("REPLACE_ACTION"))
                .andExpect(jsonPath("$.data.missions[4].reward").value(90))
                .andExpect(jsonPath("$.data.missions[4].status").isEmpty());
    }

    @DisplayName("미션의 상세 정보가 정확히 반환된다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getMissionListWithDetailedInformation() throws Exception {
        // given
        Long challengeId = 1L;

        MissionResponse mission = MissionResponse.builder()
                .missionId(1L)
                .title("상세 미션 제목")
                .content("상세 미션 내용입니다. 이것은 미션에 대한 자세한 설명입니다.")
                .category(MissionCategoryStatus.LOCATION)
                .reward(200)
                .status(null)
                .build();

        MissionListResponse response = MissionListResponse.builder()
                .challengeId(challengeId)
                .challengeTitle("상세 챌린지")
                .totalMissionCount(1)
                .totalReward(200)
                .missions(List.of(mission))
                .build();

        given(missionReadService.getMissionListByChallengeId(challengeId))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/mission/challenge/{challengeId}", challengeId)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                // ApiResponse 기본 필드 검증
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                // MissionListResponse 모든 필드(5개) 검증
                .andExpect(jsonPath("$.data.challengeId").value(challengeId))
                .andExpect(jsonPath("$.data.challengeTitle").value("상세 챌린지"))
                .andExpect(jsonPath("$.data.totalMissionCount").value(1))
                .andExpect(jsonPath("$.data.totalReward").value(200))
                .andExpect(jsonPath("$.data.missions.length()").value(1))
                // MissionResponse 모든 필드(6개) 검증
                .andExpect(jsonPath("$.data.missions[0].missionId").value(1))
                .andExpect(jsonPath("$.data.missions[0].title").value("상세 미션 제목"))
                .andExpect(jsonPath("$.data.missions[0].content").value("상세 미션 내용입니다. 이것은 미션에 대한 자세한 설명입니다."))
                .andExpect(jsonPath("$.data.missions[0].category").value("LOCATION"))
                .andExpect(jsonPath("$.data.missions[0].reward").value(200))
                .andExpect(jsonPath("$.data.missions[0].status").isEmpty());
    }

    @DisplayName("챌린지 ID가 음수인 경우에도 API 호출이 가능하다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getMissionListWithNegativeChallengeId() throws Exception {
        // given
        Long challengeId = -1L;

        MissionListResponse response = MissionListResponse.builder()
                .challengeId(challengeId)
                .challengeTitle("테스트 챌린지")
                .totalMissionCount(0)
                .totalReward(0)
                .missions(List.of())
                .build();

        given(missionReadService.getMissionListByChallengeId(challengeId))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/mission/challenge/{challengeId}", challengeId)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                // ApiResponse 기본 필드 검증
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                // MissionListResponse 모든 필드(5개) 검증
                .andExpect(jsonPath("$.data.challengeId").value(challengeId))
                .andExpect(jsonPath("$.data.challengeTitle").value("테스트 챌린지"))
                .andExpect(jsonPath("$.data.totalMissionCount").value(0))
                .andExpect(jsonPath("$.data.totalReward").value(0))
                .andExpect(jsonPath("$.data.missions").isEmpty());
    }

    @DisplayName("보상 포인트 합계가 정확히 계산되어 반환된다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getMissionListWithCorrectTotalReward() throws Exception {
        // given
        Long challengeId = 1L;

        List<MissionResponse> missions = List.of(
                MissionResponse.builder()
                        .missionId(1L)
                        .title("미션1")
                        .content("내용1")
                        .category(MissionCategoryStatus.LOCATION)
                        .reward(100)
                        .status(null)
                        .build(),
                MissionResponse.builder()
                        .missionId(2L)
                        .title("미션2")
                        .content("내용2")
                        .category(MissionCategoryStatus.REPLACE_ACTION)
                        .reward(250)
                        .status(null)
                        .build(),
                MissionResponse.builder()
                        .missionId(3L)
                        .title("미션3")
                        .content("내용3")
                        .category(MissionCategoryStatus.HOLD)
                        .reward(175)
                        .status(null)
                        .build()
        );

        MissionListResponse response = MissionListResponse.builder()
                .challengeId(challengeId)
                .challengeTitle("보상 테스트 챌린지")
                .totalMissionCount(3)
                .totalReward(525) // 100 + 250 + 175
                .missions(missions)
                .build();

        given(missionReadService.getMissionListByChallengeId(challengeId))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/mission/challenge/{challengeId}", challengeId)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                // ApiResponse 기본 필드 검증
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                // MissionListResponse 모든 필드(5개) 검증
                .andExpect(jsonPath("$.data.challengeId").value(challengeId))
                .andExpect(jsonPath("$.data.challengeTitle").value("보상 테스트 챌린지"))
                .andExpect(jsonPath("$.data.totalMissionCount").value(3))
                .andExpect(jsonPath("$.data.totalReward").value(525))
                .andExpect(jsonPath("$.data.missions.length()").value(3))
                // 첫 번째 미션 모든 필드(6개) 검증
                .andExpect(jsonPath("$.data.missions[0].missionId").value(1))
                .andExpect(jsonPath("$.data.missions[0].title").value("미션1"))
                .andExpect(jsonPath("$.data.missions[0].content").value("내용1"))
                .andExpect(jsonPath("$.data.missions[0].category").value("LOCATION"))
                .andExpect(jsonPath("$.data.missions[0].reward").value(100))
                .andExpect(jsonPath("$.data.missions[0].status").isEmpty())
                // 두 번째 미션 모든 필드(6개) 검증
                .andExpect(jsonPath("$.data.missions[1].missionId").value(2))
                .andExpect(jsonPath("$.data.missions[1].title").value("미션2"))
                .andExpect(jsonPath("$.data.missions[1].content").value("내용2"))
                .andExpect(jsonPath("$.data.missions[1].category").value("REPLACE_ACTION"))
                .andExpect(jsonPath("$.data.missions[1].reward").value(250))
                .andExpect(jsonPath("$.data.missions[1].status").isEmpty())
                // 세 번째 미션 모든 필드(6개) 검증
                .andExpect(jsonPath("$.data.missions[2].missionId").value(3))
                .andExpect(jsonPath("$.data.missions[2].title").value("미션3"))
                .andExpect(jsonPath("$.data.missions[2].content").value("내용3"))
                .andExpect(jsonPath("$.data.missions[2].category").value("HOLD"))
                .andExpect(jsonPath("$.data.missions[2].reward").value(175))
                .andExpect(jsonPath("$.data.missions[2].status").isEmpty());
    }
}
