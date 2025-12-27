package com.addiction.challenge.challengehistory.controller;

import com.addiction.ControllerTestSupport;
import com.addiction.challenge.challengehistory.service.response.ChallengeHistoryResponse;
import com.addiction.common.enums.ChallengeStatus;
import com.addiction.global.page.response.PageCustom;
import com.addiction.global.page.response.PageableCustom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ChallengeHistoryControllerTest extends ControllerTestSupport {

    @DisplayName("진행중인 챌린지를 조회한다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getProgressingChallenge() throws Exception {
        // given
        ChallengeHistoryResponse response = ChallengeHistoryResponse.builder()
                .challengeHistoryId(1L)
                .title("금연 챌린지")
                .content("30일 금연 도전")
                .badge("badge1.png")
                .reward(500)
                .status(ChallengeStatus.PROGRESSING)
                .build();

        given(challengeHistoryReadService.getProgressingChallenge())
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge-history/progressing")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.challengeHistoryId").value(1))
                .andExpect(jsonPath("$.data.title").value("금연 챌린지"))
                .andExpect(jsonPath("$.data.content").value("30일 금연 도전"))
                .andExpect(jsonPath("$.data.badge").value("badge1.png"))
                .andExpect(jsonPath("$.data.reward").value(500))
                .andExpect(jsonPath("$.data.status").value("PROGRESSING"));
    }

    @DisplayName("진행중인 챌린지가 없으면 null을 반환한다")
    @Test
    @WithMockUser(roles = "USER")
    void getProgressingChallengeWhenNull() throws Exception {
        // given
        given(challengeHistoryReadService.getProgressingChallenge())
                .willReturn(null);

        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge-history/progressing")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("단일 진행중 챌린지의 모든 필드가 정확히 반환된다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getProgressingChallengeWithAllFields() throws Exception {
        // given
        ChallengeHistoryResponse response = ChallengeHistoryResponse.builder()
                .challengeHistoryId(99L)
                .title("테스트 챌린지")
                .content("테스트 챌린지 상세 내용")
                .badge("test_badge.png")
                .reward(999)
                .status(ChallengeStatus.PROGRESSING)
                .build();

        given(challengeHistoryReadService.getProgressingChallenge())
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge-history/progressing")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.challengeHistoryId").value(99))
                .andExpect(jsonPath("$.data.title").value("테스트 챌린지"))
                .andExpect(jsonPath("$.data.content").value("테스트 챌린지 상세 내용"))
                .andExpect(jsonPath("$.data.badge").value("test_badge.png"))
                .andExpect(jsonPath("$.data.reward").value(999))
                .andExpect(jsonPath("$.data.status").value("PROGRESSING"));
    }

    @DisplayName("완료된 챌린지 목록을 페이징하여 조회한다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getFinishedChallengeList() throws Exception {
        // given
        List<ChallengeHistoryResponse> histories = List.of(
                ChallengeHistoryResponse.builder()
                        .challengeHistoryId(1L)
                        .title("챌린지1")
                        .content("내용1")
                        .badge("badge1.png")
                        .reward(100)
                        .status(ChallengeStatus.COMPLETED)
                        .build(),
                ChallengeHistoryResponse.builder()
                        .challengeHistoryId(2L)
                        .title("챌린지2")
                        .content("내용2")
                        .badge("badge2.png")
                        .reward(200)
                        .status(ChallengeStatus.COMPLETED)
                        .build(),
                ChallengeHistoryResponse.builder()
                        .challengeHistoryId(3L)
                        .title("챌린지3")
                        .content("내용3")
                        .badge("badge3.png")
                        .reward(300)
                        .status(ChallengeStatus.COMPLETED)
                        .build()
        );

        PageableCustom pageInfo = PageableCustom.builder()
                .currentPage(1)
                .totalPage(1)
                .totalElement(3L)
                .build();

        PageCustom<ChallengeHistoryResponse> response = PageCustom.<ChallengeHistoryResponse>builder()
                .content(histories)
                .pageInfo(pageInfo)
                .build();

        given(challengeHistoryReadService.getFinishedChallengeList(any()))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge-history/finished")
                                .param("page", "1")
                                .param("size", "10")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(3))
                .andExpect(jsonPath("$.data.pageInfo").exists())
                .andExpect(jsonPath("$.data.pageInfo.currentPage").value(1))
                .andExpect(jsonPath("$.data.pageInfo.totalPage").value(1))
                .andExpect(jsonPath("$.data.pageInfo.totalElement").value(3))
                .andExpect(jsonPath("$.data.content[0].challengeHistoryId").value(1))
                .andExpect(jsonPath("$.data.content[0].title").value("챌린지1"))
                .andExpect(jsonPath("$.data.content[0].content").value("내용1"))
                .andExpect(jsonPath("$.data.content[0].badge").value("badge1.png"))
                .andExpect(jsonPath("$.data.content[0].reward").value(100))
                .andExpect(jsonPath("$.data.content[0].status").value("COMPLETED"))
                .andExpect(jsonPath("$.data.content[1].challengeHistoryId").value(2))
                .andExpect(jsonPath("$.data.content[1].title").value("챌린지2"))
                .andExpect(jsonPath("$.data.content[1].content").value("내용2"))
                .andExpect(jsonPath("$.data.content[1].badge").value("badge2.png"))
                .andExpect(jsonPath("$.data.content[1].reward").value(200))
                .andExpect(jsonPath("$.data.content[1].status").value("COMPLETED"))
                .andExpect(jsonPath("$.data.content[2].challengeHistoryId").value(3))
                .andExpect(jsonPath("$.data.content[2].title").value("챌린지3"))
                .andExpect(jsonPath("$.data.content[2].content").value("내용3"))
                .andExpect(jsonPath("$.data.content[2].badge").value("badge3.png"))
                .andExpect(jsonPath("$.data.content[2].reward").value(300))
                .andExpect(jsonPath("$.data.content[2].status").value("COMPLETED"));
    }

    @DisplayName("완료된 챌린지가 없는 경우 빈 목록을 반환한다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getFinishedChallengeListWhenEmpty() throws Exception {
        // given
        PageableCustom pageInfo = PageableCustom.builder()
                .currentPage(1)
                .totalPage(0)
                .totalElement(0L)
                .build();

        PageCustom<ChallengeHistoryResponse> response = PageCustom.<ChallengeHistoryResponse>builder()
                .content(List.of())
                .pageInfo(pageInfo)
                .build();

        given(challengeHistoryReadService.getFinishedChallengeList(any()))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge-history/finished")
                                .param("page", "1")
                                .param("size", "10")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.content").isEmpty())
                .andExpect(jsonPath("$.data.pageInfo").exists())
                .andExpect(jsonPath("$.data.pageInfo.currentPage").value(1))
                .andExpect(jsonPath("$.data.pageInfo.totalPage").value(0))
                .andExpect(jsonPath("$.data.pageInfo.totalElement").value(0));
    }

    @DisplayName("페이징 파라미터로 2페이지를 조회한다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getFinishedChallengeListPage2() throws Exception {
        // given
        List<ChallengeHistoryResponse> histories = List.of(
                ChallengeHistoryResponse.builder()
                        .challengeHistoryId(6L)
                        .title("챌린지6")
                        .content("내용6")
                        .badge("badge6.png")
                        .reward(600)
                        .status(ChallengeStatus.COMPLETED)
                        .build(),
                ChallengeHistoryResponse.builder()
                        .challengeHistoryId(7L)
                        .title("챌린지7")
                        .content("내용7")
                        .badge("badge7.png")
                        .reward(700)
                        .status(ChallengeStatus.COMPLETED)
                        .build()
        );

        PageableCustom pageInfo = PageableCustom.builder()
                .currentPage(2)
                .totalPage(3)
                .totalElement(15L)
                .build();

        PageCustom<ChallengeHistoryResponse> response = PageCustom.<ChallengeHistoryResponse>builder()
                .content(histories)
                .pageInfo(pageInfo)
                .build();

        given(challengeHistoryReadService.getFinishedChallengeList(any()))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge-history/finished")
                                .param("page", "2")
                                .param("size", "5")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.content.length()").value(2))
                .andExpect(jsonPath("$.data.pageInfo.currentPage").value(2))
                .andExpect(jsonPath("$.data.pageInfo.totalPage").value(3))
                .andExpect(jsonPath("$.data.pageInfo.totalElement").value(15));
    }

    @DisplayName("단일 완료된 챌린지의 모든 필드가 정확히 반환된다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getSingleFinishedChallengeWithAllFields() throws Exception {
        // given
        ChallengeHistoryResponse history = ChallengeHistoryResponse.builder()
                .challengeHistoryId(1L)
                .title("테스트 완료 챌린지")
                .content("테스트 완료 챌린지 상세")
                .badge("complete_badge.png")
                .reward(777)
                .status(ChallengeStatus.COMPLETED)
                .build();

        PageableCustom pageInfo = PageableCustom.builder()
                .currentPage(1)
                .totalPage(1)
                .totalElement(1L)
                .build();

        PageCustom<ChallengeHistoryResponse> response = PageCustom.<ChallengeHistoryResponse>builder()
                .content(List.of(history))
                .pageInfo(pageInfo)
                .build();

        given(challengeHistoryReadService.getFinishedChallengeList(any()))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge-history/finished")
                                .param("page", "1")
                                .param("size", "10")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.pageInfo.currentPage").value(1))
                .andExpect(jsonPath("$.data.pageInfo.totalPage").value(1))
                .andExpect(jsonPath("$.data.pageInfo.totalElement").value(1))
                .andExpect(jsonPath("$.data.content[0].challengeHistoryId").value(1))
                .andExpect(jsonPath("$.data.content[0].title").value("테스트 완료 챌린지"))
                .andExpect(jsonPath("$.data.content[0].content").value("테스트 완료 챌린지 상세"))
                .andExpect(jsonPath("$.data.content[0].badge").value("complete_badge.png"))
                .andExpect(jsonPath("$.data.content[0].reward").value(777))
                .andExpect(jsonPath("$.data.content[0].status").value("COMPLETED"));
    }

    @DisplayName("마지막 페이지를 조회한다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getFinishedChallengeListLastPage() throws Exception {
        // given
        List<ChallengeHistoryResponse> histories = List.of(
                ChallengeHistoryResponse.builder()
                        .challengeHistoryId(11L)
                        .title("챌린지11")
                        .content("내용11")
                        .badge("badge11.png")
                        .reward(1100)
                        .status(ChallengeStatus.COMPLETED)
                        .build(),
                ChallengeHistoryResponse.builder()
                        .challengeHistoryId(12L)
                        .title("챌린지12")
                        .content("내용12")
                        .badge("badge12.png")
                        .reward(1200)
                        .status(ChallengeStatus.COMPLETED)
                        .build()
        );

        PageableCustom pageInfo = PageableCustom.builder()
                .currentPage(3)
                .totalPage(3)
                .totalElement(12L)
                .build();

        PageCustom<ChallengeHistoryResponse> response = PageCustom.<ChallengeHistoryResponse>builder()
                .content(histories)
                .pageInfo(pageInfo)
                .build();

        given(challengeHistoryReadService.getFinishedChallengeList(any()))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge-history/finished")
                                .param("page", "3")
                                .param("size", "5")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.content.length()").value(2))
                .andExpect(jsonPath("$.data.pageInfo.currentPage").value(3))
                .andExpect(jsonPath("$.data.pageInfo.totalPage").value(3))
                .andExpect(jsonPath("$.data.pageInfo.totalElement").value(12));
    }
}
