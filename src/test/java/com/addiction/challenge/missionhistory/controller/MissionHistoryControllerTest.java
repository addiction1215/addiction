package com.addiction.challenge.missionhistory.controller;

import com.addiction.ControllerTestSupport;
import com.addiction.challenge.missionhistory.service.response.MissionHistoryResponse;
import com.addiction.challenge.missionhistory.service.response.MissionProgressResponse;
import com.addiction.common.enums.MissionCategoryStatus;
import com.addiction.common.enums.MissionStatus;
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

class MissionHistoryControllerTest extends ControllerTestSupport {

    @DisplayName("챌린지 이력 ID로 미션 진행 상황을 조회한다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getMissionProgress() throws Exception {
        // given
        Long challengeHistoryId = 1L;

        List<MissionHistoryResponse> missions = List.of(
                MissionHistoryResponse.builder()
                        .missionHistoryId(101L)
                        .missionId(1L)
                        .missionTitle("평소 흡연 루트 피해보기")
                        .missionContent("평소 다니는 흡연 루트를 피해서 이동하기")
                        .category(MissionCategoryStatus.LOCATION)
                        .reward(50)
                        .status(MissionStatus.COMPLETED)
                        .build(),
                MissionHistoryResponse.builder()
                        .missionHistoryId(102L)
                        .missionId(2L)
                        .missionTitle("평소 흡연 시간대 산책하기")
                        .missionContent("평소 흡연하는 시간대에 산책해보기")
                        .category(MissionCategoryStatus.REPLACE_ACTION)
                        .reward(70)
                        .status(MissionStatus.COMPLETED)
                        .build(),
                MissionHistoryResponse.builder()
                        .missionHistoryId(103L)
                        .missionId(3L)
                        .missionTitle("5분간 참기")
                        .missionContent("흡연 욕구가 생길 때 5분간 참아보기")
                        .category(MissionCategoryStatus.HOLD)
                        .reward(30)
                        .status(MissionStatus.PROGRESSING)
                        .build()
        );

        MissionProgressResponse response = MissionProgressResponse.builder()
                .challengeHistoryId(challengeHistoryId)
                .challengeId(1L)
                .challengeTitle("유혹 피하기")
                .totalMissionCount(3)
                .completedMissionCount(2)
                .totalEarnedReward(120)
                .totalPossibleReward(150)
                .missions(missions)
                .build();

        given(missionHistoryReadService.getMissionProgress(challengeHistoryId))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/mission-history/progress/{challengeHistoryId}", challengeHistoryId)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                // MissionProgressResponse 모든 필드(8개) 검증
                .andExpect(jsonPath("$.challengeHistoryId").value(challengeHistoryId))
                .andExpect(jsonPath("$.challengeId").value(1))
                .andExpect(jsonPath("$.challengeTitle").value("유혹 피하기"))
                .andExpect(jsonPath("$.totalMissionCount").value(3))
                .andExpect(jsonPath("$.completedMissionCount").value(2))
                .andExpect(jsonPath("$.totalEarnedReward").value(120))
                .andExpect(jsonPath("$.totalPossibleReward").value(150))
                .andExpect(jsonPath("$.missions").isArray())
                .andExpect(jsonPath("$.missions.length()").value(3))
                // 첫 번째 MissionHistoryResponse 모든 필드(7개) 검증
                .andExpect(jsonPath("$.missions[0].missionHistoryId").value(101))
                .andExpect(jsonPath("$.missions[0].missionId").value(1))
                .andExpect(jsonPath("$.missions[0].missionTitle").value("평소 흡연 루트 피해보기"))
                .andExpect(jsonPath("$.missions[0].missionContent").value("평소 다니는 흡연 루트를 피해서 이동하기"))
                .andExpect(jsonPath("$.missions[0].category").value("LOCATION"))
                .andExpect(jsonPath("$.missions[0].reward").value(50))
                .andExpect(jsonPath("$.missions[0].status").value("COMPLETED"))
                // 두 번째 MissionHistoryResponse 모든 필드(7개) 검증
                .andExpect(jsonPath("$.missions[1].missionHistoryId").value(102))
                .andExpect(jsonPath("$.missions[1].missionId").value(2))
                .andExpect(jsonPath("$.missions[1].missionTitle").value("평소 흡연 시간대 산책하기"))
                .andExpect(jsonPath("$.missions[1].missionContent").value("평소 흡연하는 시간대에 산책해보기"))
                .andExpect(jsonPath("$.missions[1].category").value("REPLACE_ACTION"))
                .andExpect(jsonPath("$.missions[1].reward").value(70))
                .andExpect(jsonPath("$.missions[1].status").value("COMPLETED"))
                // 세 번째 MissionHistoryResponse 모든 필드(7개) 검증
                .andExpect(jsonPath("$.missions[2].missionHistoryId").value(103))
                .andExpect(jsonPath("$.missions[2].missionId").value(3))
                .andExpect(jsonPath("$.missions[2].missionTitle").value("5분간 참기"))
                .andExpect(jsonPath("$.missions[2].missionContent").value("흡연 욕구가 생길 때 5분간 참아보기"))
                .andExpect(jsonPath("$.missions[2].category").value("HOLD"))
                .andExpect(jsonPath("$.missions[2].reward").value(30))
                .andExpect(jsonPath("$.missions[2].status").value("PROGRESSING"));
    }

    @DisplayName("모든 미션이 완료된 경우 진행 상황을 조회한다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getMissionProgressWithAllCompleted() throws Exception {
        // given
        Long challengeHistoryId = 1L;

        List<MissionHistoryResponse> missions = List.of(
                MissionHistoryResponse.builder()
                        .missionHistoryId(101L)
                        .missionId(1L)
                        .missionTitle("미션1")
                        .missionContent("내용1")
                        .category(MissionCategoryStatus.LOCATION)
                        .reward(100)
                        .status(MissionStatus.COMPLETED)
                        .build(),
                MissionHistoryResponse.builder()
                        .missionHistoryId(102L)
                        .missionId(2L)
                        .missionTitle("미션2")
                        .missionContent("내용2")
                        .category(MissionCategoryStatus.HOLD)
                        .reward(150)
                        .status(MissionStatus.COMPLETED)
                        .build()
        );

        MissionProgressResponse response = MissionProgressResponse.builder()
                .challengeHistoryId(challengeHistoryId)
                .challengeId(1L)
                .challengeTitle("완료 챌린지")
                .totalMissionCount(2)
                .completedMissionCount(2)
                .totalEarnedReward(250)
                .totalPossibleReward(250)
                .missions(missions)
                .build();

        given(missionHistoryReadService.getMissionProgress(challengeHistoryId))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/mission-history/progress/{challengeHistoryId}", challengeHistoryId)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                // MissionProgressResponse 모든 필드(8개) 검증
                .andExpect(jsonPath("$.challengeHistoryId").value(challengeHistoryId))
                .andExpect(jsonPath("$.challengeId").value(1))
                .andExpect(jsonPath("$.challengeTitle").value("완료 챌린지"))
                .andExpect(jsonPath("$.totalMissionCount").value(2))
                .andExpect(jsonPath("$.completedMissionCount").value(2))
                .andExpect(jsonPath("$.totalEarnedReward").value(250))
                .andExpect(jsonPath("$.totalPossibleReward").value(250))
                .andExpect(jsonPath("$.missions.length()").value(2));
    }

    @DisplayName("모든 미션이 진행중인 경우 진행 상황을 조회한다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getMissionProgressWithNoneCompleted() throws Exception {
        // given
        Long challengeHistoryId = 1L;

        List<MissionHistoryResponse> missions = List.of(
                MissionHistoryResponse.builder()
                        .missionHistoryId(101L)
                        .missionId(1L)
                        .missionTitle("미션1")
                        .missionContent("내용1")
                        .category(MissionCategoryStatus.LOCATION)
                        .reward(80)
                        .status(MissionStatus.PROGRESSING)
                        .build(),
                MissionHistoryResponse.builder()
                        .missionHistoryId(102L)
                        .missionId(2L)
                        .missionTitle("미션2")
                        .missionContent("내용2")
                        .category(MissionCategoryStatus.REPLACE_ACTION)
                        .reward(90)
                        .status(MissionStatus.PROGRESSING)
                        .build()
        );

        MissionProgressResponse response = MissionProgressResponse.builder()
                .challengeHistoryId(challengeHistoryId)
                .challengeId(1L)
                .challengeTitle("시작 챌린지")
                .totalMissionCount(2)
                .completedMissionCount(0)
                .totalEarnedReward(0)
                .totalPossibleReward(170)
                .missions(missions)
                .build();

        given(missionHistoryReadService.getMissionProgress(challengeHistoryId))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/mission-history/progress/{challengeHistoryId}", challengeHistoryId)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                // MissionProgressResponse 모든 필드(8개) 검증
                .andExpect(jsonPath("$.challengeHistoryId").value(challengeHistoryId))
                .andExpect(jsonPath("$.challengeId").value(1))
                .andExpect(jsonPath("$.challengeTitle").value("시작 챌린지"))
                .andExpect(jsonPath("$.totalMissionCount").value(2))
                .andExpect(jsonPath("$.completedMissionCount").value(0))
                .andExpect(jsonPath("$.totalEarnedReward").value(0))
                .andExpect(jsonPath("$.totalPossibleReward").value(170))
                .andExpect(jsonPath("$.missions.length()").value(2));
    }

    @DisplayName("미션 이력이 없는 경우 빈 목록을 반환한다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getMissionProgressWithNoMissionHistory() throws Exception {
        // given
        Long challengeHistoryId = 1L;

        MissionProgressResponse response = MissionProgressResponse.builder()
                .challengeHistoryId(challengeHistoryId)
                .challengeId(1L)
                .challengeTitle("빈 챌린지")
                .totalMissionCount(0)
                .completedMissionCount(0)
                .totalEarnedReward(0)
                .totalPossibleReward(0)
                .missions(List.of())
                .build();

        given(missionHistoryReadService.getMissionProgress(challengeHistoryId))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/mission-history/progress/{challengeHistoryId}", challengeHistoryId)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                // MissionProgressResponse 모든 필드(8개) 검증
                .andExpect(jsonPath("$.challengeHistoryId").value(challengeHistoryId))
                .andExpect(jsonPath("$.challengeId").value(1))
                .andExpect(jsonPath("$.challengeTitle").value("빈 챌린지"))
                .andExpect(jsonPath("$.totalMissionCount").value(0))
                .andExpect(jsonPath("$.completedMissionCount").value(0))
                .andExpect(jsonPath("$.totalEarnedReward").value(0))
                .andExpect(jsonPath("$.totalPossibleReward").value(0))
                .andExpect(jsonPath("$.missions").isEmpty());
    }

    @DisplayName("일부 미션만 완료된 경우 진행 상황을 조회한다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getMissionProgressWithPartialCompletion() throws Exception {
        // given
        Long challengeHistoryId = 1L;

        List<MissionHistoryResponse> missions = List.of(
                MissionHistoryResponse.builder()
                        .missionHistoryId(101L)
                        .missionId(1L)
                        .missionTitle("미션1")
                        .missionContent("내용1")
                        .category(MissionCategoryStatus.LOCATION)
                        .reward(100)
                        .status(MissionStatus.COMPLETED)
                        .build(),
                MissionHistoryResponse.builder()
                        .missionHistoryId(102L)
                        .missionId(2L)
                        .missionTitle("미션2")
                        .missionContent("내용2")
                        .category(MissionCategoryStatus.REPLACE_ACTION)
                        .reward(100)
                        .status(MissionStatus.PROGRESSING)
                        .build(),
                MissionHistoryResponse.builder()
                        .missionHistoryId(103L)
                        .missionId(3L)
                        .missionTitle("미션3")
                        .missionContent("내용3")
                        .category(MissionCategoryStatus.HOLD)
                        .reward(100)
                        .status(MissionStatus.COMPLETED)
                        .build(),
                MissionHistoryResponse.builder()
                        .missionHistoryId(104L)
                        .missionId(4L)
                        .missionTitle("미션4")
                        .missionContent("내용4")
                        .category(MissionCategoryStatus.LOCATION)
                        .reward(100)
                        .status(MissionStatus.PROGRESSING)
                        .build()
        );

        MissionProgressResponse response = MissionProgressResponse.builder()
                .challengeHistoryId(challengeHistoryId)
                .challengeId(1L)
                .challengeTitle("부분 완료 챌린지")
                .totalMissionCount(4)
                .completedMissionCount(2)
                .totalEarnedReward(200)
                .totalPossibleReward(400)
                .missions(missions)
                .build();

        given(missionHistoryReadService.getMissionProgress(challengeHistoryId))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/mission-history/progress/{challengeHistoryId}", challengeHistoryId)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                // MissionProgressResponse 모든 필드(8개) 검증
                .andExpect(jsonPath("$.challengeHistoryId").value(challengeHistoryId))
                .andExpect(jsonPath("$.challengeId").value(1))
                .andExpect(jsonPath("$.challengeTitle").value("부분 완료 챌린지"))
                .andExpect(jsonPath("$.totalMissionCount").value(4))
                .andExpect(jsonPath("$.completedMissionCount").value(2))
                .andExpect(jsonPath("$.totalEarnedReward").value(200))
                .andExpect(jsonPath("$.totalPossibleReward").value(400))
                .andExpect(jsonPath("$.missions.length()").value(4))
                // 첫 번째 미션 - 완료
                .andExpect(jsonPath("$.missions[0].status").value("COMPLETED"))
                // 두 번째 미션 - 진행중
                .andExpect(jsonPath("$.missions[1].status").value("PROGRESSING"))
                // 세 번째 미션 - 완료
                .andExpect(jsonPath("$.missions[2].status").value("COMPLETED"))
                // 네 번째 미션 - 진행중
                .andExpect(jsonPath("$.missions[3].status").value("PROGRESSING"));
    }

    @DisplayName("단일 미션 이력의 모든 필드가 정확히 반환된다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getSingleMissionHistoryWithAllFields() throws Exception {
        // given
        Long challengeHistoryId = 1L;

        MissionHistoryResponse mission = MissionHistoryResponse.builder()
                .missionHistoryId(101L)
                .missionId(1L)
                .missionTitle("테스트 미션")
                .missionContent("테스트 미션 내용")
                .category(MissionCategoryStatus.REPLACE_ACTION)
                .reward(85)
                .status(MissionStatus.COMPLETED)
                .build();

        MissionProgressResponse response = MissionProgressResponse.builder()
                .challengeHistoryId(challengeHistoryId)
                .challengeId(1L)
                .challengeTitle("단일 미션 챌린지")
                .totalMissionCount(1)
                .completedMissionCount(1)
                .totalEarnedReward(85)
                .totalPossibleReward(85)
                .missions(List.of(mission))
                .build();

        given(missionHistoryReadService.getMissionProgress(challengeHistoryId))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/mission-history/progress/{challengeHistoryId}", challengeHistoryId)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                // MissionProgressResponse 모든 필드(8개) 검증
                .andExpect(jsonPath("$.challengeHistoryId").value(challengeHistoryId))
                .andExpect(jsonPath("$.challengeId").value(1))
                .andExpect(jsonPath("$.challengeTitle").value("단일 미션 챌린지"))
                .andExpect(jsonPath("$.totalMissionCount").value(1))
                .andExpect(jsonPath("$.completedMissionCount").value(1))
                .andExpect(jsonPath("$.totalEarnedReward").value(85))
                .andExpect(jsonPath("$.totalPossibleReward").value(85))
                .andExpect(jsonPath("$.missions.length()").value(1))
                // MissionHistoryResponse 모든 필드(7개) 검증
                .andExpect(jsonPath("$.missions[0].missionHistoryId").value(101))
                .andExpect(jsonPath("$.missions[0].missionId").value(1))
                .andExpect(jsonPath("$.missions[0].missionTitle").value("테스트 미션"))
                .andExpect(jsonPath("$.missions[0].missionContent").value("테스트 미션 내용"))
                .andExpect(jsonPath("$.missions[0].category").value("REPLACE_ACTION"))
                .andExpect(jsonPath("$.missions[0].reward").value(85))
                .andExpect(jsonPath("$.missions[0].status").value("COMPLETED"));
    }
}
