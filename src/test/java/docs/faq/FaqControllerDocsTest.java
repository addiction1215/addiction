package docs.faq;

import com.addiction.faq.controller.FaqController;
import com.addiction.faq.entity.enums.FaqCategory;
import com.addiction.faq.service.FaqReadService;
import com.addiction.faq.service.request.FaqListServiceRequest;
import com.addiction.faq.service.response.FaqListResponse;
import com.addiction.global.page.response.PageCustom;
import com.addiction.global.page.response.PageableCustom;
import docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FaqControllerDocsTest extends RestDocsSupport {

    private final FaqReadService faqReadService = mock(FaqReadService.class);

    @Override
    protected Object initController() {
        return new FaqController(faqReadService);
    }

    @DisplayName("FAQ 리스트를 조회한다.")
    @Test
    void FAQ_리스트를_조회한다() throws Exception {
        given(faqReadService.findAll(org.mockito.ArgumentMatchers.any(FaqListServiceRequest.class)))
                .willReturn(PageCustom.<FaqListResponse>builder()
                        .content(List.of(
                        FaqListResponse.builder()
                                .id(1L)
                                .category(FaqCategory.INFO_FRIEND)
                                .pinned(true)
                                .sortOrder(1)
                                .title("자주 묻는 질문 1")
                                .description("자주 묻는 질문 1에 대한 답변입니다.")
                                .build(),
                        FaqListResponse.builder()
                                .id(2L)
                                .category(FaqCategory.CHALLENGE)
                                .pinned(false)
                                .sortOrder(2)
                                .title("자주 묻는 질문 2")
                                .description("자주 묻는 질문 2에 대한 답변입니다.")
                                .build()
                        ))
                        .pageInfo(PageableCustom.builder()
                                .currentPage(1)
                                .totalPage(3)
                                .totalElement(22L)
                                .build())
                        .build());

        mockMvc.perform(
                        get("/api/v1/faq")
                                .queryParam("page", "1")
                                .queryParam("size", "10")
                                .queryParam("category", "INFO_FRIEND")
                                .queryParam("keyword", "질문")
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("faq-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호, 1부터 시작"),
                                parameterWithName("size").description("페이지 크기"),
                                parameterWithName("category").description("FAQ 카테고리: INFO_FRIEND, REPORT, LEADERBOARD, CHALLENGE"),
                                parameterWithName("keyword").description("제목/내용 검색어")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("httpStatus").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data.content").type(JsonFieldType.ARRAY)
                                        .description("FAQ 목록"),
                                fieldWithPath("data.content[].id").type(JsonFieldType.NUMBER)
                                        .description("FAQ ID"),
                                fieldWithPath("data.content[].category").type(JsonFieldType.STRING)
                                        .description("FAQ 카테고리"),
                                fieldWithPath("data.content[].pinned").type(JsonFieldType.BOOLEAN)
                                        .description("상단 고정 여부"),
                                fieldWithPath("data.content[].sortOrder").type(JsonFieldType.NUMBER)
                                        .description("정렬 순서"),
                                fieldWithPath("data.content[].title").type(JsonFieldType.STRING)
                                        .description("FAQ 제목"),
                                fieldWithPath("data.content[].description").type(JsonFieldType.STRING)
                                        .description("FAQ 내용"),
                                fieldWithPath("data.pageInfo.currentPage").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지"),
                                fieldWithPath("data.pageInfo.totalPage").type(JsonFieldType.NUMBER)
                                        .description("전체 페이지 수"),
                                fieldWithPath("data.pageInfo.totalElement").type(JsonFieldType.NUMBER)
                                        .description("전체 데이터 수")
                        )
                ));
    }
}
