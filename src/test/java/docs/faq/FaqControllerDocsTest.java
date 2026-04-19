package docs.faq;

import com.addiction.faq.controller.FaqController;
import com.addiction.faq.service.FaqReadService;
import com.addiction.faq.service.response.FaqListResponse;
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
        given(faqReadService.findAll())
                .willReturn(List.of(
                        FaqListResponse.builder()
                                .id(1L)
                                .title("자주 묻는 질문 1")
                                .description("자주 묻는 질문 1에 대한 답변입니다.")
                                .build(),
                        FaqListResponse.builder()
                                .id(2L)
                                .title("자주 묻는 질문 2")
                                .description("자주 묻는 질문 2에 대한 답변입니다.")
                                .build()
                ));

        mockMvc.perform(
                        get("/api/v1/faq")
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("faq-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("httpStatus").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY)
                                        .description("FAQ 목록"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER)
                                        .description("FAQ ID"),
                                fieldWithPath("data[].title").type(JsonFieldType.STRING)
                                        .description("FAQ 제목"),
                                fieldWithPath("data[].description").type(JsonFieldType.STRING)
                                        .description("FAQ 내용")
                        )
                ));
    }
}
