package com.addiction.challenge.challange.controller;

import com.addiction.ControllerTestSupport;
import com.addiction.challenge.challange.service.response.ChallengeResponse;
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

class ChallengeControllerTest extends ControllerTestSupport {

    @DisplayName("남은 챌린지 목록을 페이징하여 조회한다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getLeftChallengeList() throws Exception {
        // given
        List<ChallengeResponse> challenges = List.of(
                ChallengeResponse.builder()
                        .challengeId(3L)
                        .title("챌린지3")
                        .content("내용3")
                        .badge("badge3.png")
                        .reward(300)
                        .build(),
                ChallengeResponse.builder()
                        .challengeId(4L)
                        .title("챌린지4")
                        .content("내용4")
                        .badge("badge4.png")
                        .reward(400)
                        .build(),
                ChallengeResponse.builder()
                        .challengeId(5L)
                        .title("챌린지5")
                        .content("내용5")
                        .badge("badge5.png")
                        .reward(500)
                        .build()
        );

        PageableCustom pageInfo = PageableCustom.builder()
                .currentPage(1)
                .totalPage(1)
                .totalElement(3L)
                .build();

        PageCustom<ChallengeResponse> response = PageCustom.<ChallengeResponse>builder()
                .content(challenges)
                .pageInfo(pageInfo)
                .build();

        given(challengeReadService.getLeftChallengeList(any()))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge/left")
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
                .andExpect(jsonPath("$.data.content[0].challengeId").value(3))
                .andExpect(jsonPath("$.data.content[0].title").value("챌린지3"))
                .andExpect(jsonPath("$.data.content[0].content").value("내용3"))
                .andExpect(jsonPath("$.data.content[0].badge").value("badge3.png"))
                .andExpect(jsonPath("$.data.content[0].reward").value(300))
                .andExpect(jsonPath("$.data.content[1].challengeId").value(4))
                .andExpect(jsonPath("$.data.content[1].title").value("챌린지4"))
                .andExpect(jsonPath("$.data.content[1].content").value("내용4"))
                .andExpect(jsonPath("$.data.content[1].badge").value("badge4.png"))
                .andExpect(jsonPath("$.data.content[1].reward").value(400))
                .andExpect(jsonPath("$.data.content[2].challengeId").value(5))
                .andExpect(jsonPath("$.data.content[2].title").value("챌린지5"))
                .andExpect(jsonPath("$.data.content[2].content").value("내용5"))
                .andExpect(jsonPath("$.data.content[2].badge").value("badge5.png"))
                .andExpect(jsonPath("$.data.content[2].reward").value(500));
    }

    @DisplayName("남은 챌린지가 없는 경우 빈 목록을 반환한다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getLeftChallengeListWhenEmpty() throws Exception {
        // given
        PageableCustom pageInfo = PageableCustom.builder()
                .currentPage(1)
                .totalPage(0)
                .totalElement(0L)
                .build();

        PageCustom<ChallengeResponse> response = PageCustom.<ChallengeResponse>builder()
                .content(List.of())
                .pageInfo(pageInfo)
                .build();

        given(challengeReadService.getLeftChallengeList(any()))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge/left")
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
    void getLeftChallengeListPage2() throws Exception {
        // given
        List<ChallengeResponse> challenges = List.of(
                ChallengeResponse.builder()
                        .challengeId(6L)
                        .title("챌린지6")
                        .content("내용6")
                        .badge("badge6.png")
                        .reward(600)
                        .build(),
                ChallengeResponse.builder()
                        .challengeId(7L)
                        .title("챌린지7")
                        .content("내용7")
                        .badge("badge7.png")
                        .reward(700)
                        .build()
        );

        PageableCustom pageInfo = PageableCustom.builder()
                .currentPage(2)
                .totalPage(3)
                .totalElement(15L)
                .build();

        PageCustom<ChallengeResponse> response = PageCustom.<ChallengeResponse>builder()
                .content(challenges)
                .pageInfo(pageInfo)
                .build();

        given(challengeReadService.getLeftChallengeList(any()))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge/left")
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

    @DisplayName("단일 챌린지의 모든 필드가 정확히 반환된다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getSingleChallengeWithAllFields() throws Exception {
        // given
        ChallengeResponse challenge = ChallengeResponse.builder()
                .challengeId(1L)
                .title("테스트 챌린지")
                .content("테스트 챌린지 상세 내용")
                .badge("test_badge.png")
                .reward(999)
                .build();

        PageableCustom pageInfo = PageableCustom.builder()
                .currentPage(1)
                .totalPage(1)
                .totalElement(1L)
                .build();

        PageCustom<ChallengeResponse> response = PageCustom.<ChallengeResponse>builder()
                .content(List.of(challenge))
                .pageInfo(pageInfo)
                .build();

        given(challengeReadService.getLeftChallengeList(any()))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge/left")
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
                .andExpect(jsonPath("$.data.content[0].challengeId").value(1))
                .andExpect(jsonPath("$.data.content[0].title").value("테스트 챌린지"))
                .andExpect(jsonPath("$.data.content[0].content").value("테스트 챌린지 상세 내용"))
                .andExpect(jsonPath("$.data.content[0].badge").value("test_badge.png"))
                .andExpect(jsonPath("$.data.content[0].reward").value(999));
    }

    @DisplayName("페이지 크기를 다르게 설정하여 조회한다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getLeftChallengeListWithDifferentPageSize() throws Exception {
        // given
        List<ChallengeResponse> challenges = List.of(
                ChallengeResponse.builder()
                        .challengeId(1L)
                        .title("챌린지1")
                        .content("내용1")
                        .badge("badge1.png")
                        .reward(100)
                        .build(),
                ChallengeResponse.builder()
                        .challengeId(2L)
                        .title("챌린지2")
                        .content("내용2")
                        .badge("badge2.png")
                        .reward(200)
                        .build(),
                ChallengeResponse.builder()
                        .challengeId(3L)
                        .title("챌린지3")
                        .content("내용3")
                        .badge("badge3.png")
                        .reward(300)
                        .build()
        );

        PageableCustom pageInfo = PageableCustom.builder()
                .currentPage(1)
                .totalPage(2)
                .totalElement(6L)
                .build();

        PageCustom<ChallengeResponse> response = PageCustom.<ChallengeResponse>builder()
                .content(challenges)
                .pageInfo(pageInfo)
                .build();

        given(challengeReadService.getLeftChallengeList(any()))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge/left")
                                .param("page", "1")
                                .param("size", "3")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.content.length()").value(3))
                .andExpect(jsonPath("$.data.pageInfo.currentPage").value(1))
                .andExpect(jsonPath("$.data.pageInfo.totalPage").value(2))
                .andExpect(jsonPath("$.data.pageInfo.totalElement").value(6))
                .andExpect(jsonPath("$.data.content[0].challengeId").value(1))
                .andExpect(jsonPath("$.data.content[0].title").value("챌린지1"))
                .andExpect(jsonPath("$.data.content[1].challengeId").value(2))
                .andExpect(jsonPath("$.data.content[1].title").value("챌린지2"))
                .andExpect(jsonPath("$.data.content[2].challengeId").value(3))
                .andExpect(jsonPath("$.data.content[2].title").value("챌린지3"));
    }

    @DisplayName("마지막 페이지를 조회한다 - 모든 필드 검증")
    @Test
    @WithMockUser(roles = "USER")
    void getLeftChallengeListLastPage() throws Exception {
        // given
        List<ChallengeResponse> challenges = List.of(
                ChallengeResponse.builder()
                        .challengeId(11L)
                        .title("챌린지11")
                        .content("내용11")
                        .badge("badge11.png")
                        .reward(1100)
                        .build(),
                ChallengeResponse.builder()
                        .challengeId(12L)
                        .title("챌린지12")
                        .content("내용12")
                        .badge("badge12.png")
                        .reward(1200)
                        .build()
        );

        PageableCustom pageInfo = PageableCustom.builder()
                .currentPage(3)
                .totalPage(3)
                .totalElement(12L)
                .build();

        PageCustom<ChallengeResponse> response = PageCustom.<ChallengeResponse>builder()
                .content(challenges)
                .pageInfo(pageInfo)
                .build();

        given(challengeReadService.getLeftChallengeList(any()))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/challenge/left")
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
