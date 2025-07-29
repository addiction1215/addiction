package docs.inquiry;

import com.addiction.inquiry.inquryQuestion.controller.InquiryQuestionController;
import com.addiction.inquiry.inquryQuestion.controller.request.InquiryQuestionSaveRequest;
import com.addiction.inquiry.inquryQuestion.enums.InquiryStatus;
import com.addiction.inquiry.inquryQuestion.service.InquiryQuestionReadService;
import com.addiction.inquiry.inquryQuestion.service.InquiryQuestionService;
import com.addiction.inquiry.inquryQuestion.service.response.InquiryQuestionFindResponse;
import com.addiction.inquiry.inquryQuestion.service.response.InquiryQuestionSaveResponse;
import docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InquiryQuestionControllerDocsTest extends RestDocsSupport {

    private final InquiryQuestionReadService inquiryQuestionReadService = mock(InquiryQuestionReadService.class);
    private final InquiryQuestionService inquiryQuestionService = mock(InquiryQuestionService.class);

    @Override
    protected Object initController() {
        return new InquiryQuestionController(inquiryQuestionReadService, inquiryQuestionService);
    }

    @DisplayName("문의 질문을 등록한다.")
    @Test
    void 문의_질문을_등록한다() throws Exception {
        // given
        InquiryQuestionSaveRequest request = InquiryQuestionSaveRequest.builder()
                .title("문의 제목입니다")
                .question("문의 내용입니다")
                .build();

        given(inquiryQuestionService.save(any()))
                .willReturn(InquiryQuestionSaveResponse.builder()
                        .userId(1)
                        .title("문의 제목입니다")
                        .question("문의 내용입니다")
                        .build()
                );

        // when // then
        mockMvc.perform(
                        post("/api/v1/inquiry/question")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("inquiry-question-save",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("문의 제목"),
                                fieldWithPath("question").type(JsonFieldType.STRING)
                                        .description("문의 내용")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("httpStatus").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.userId").type(JsonFieldType.NUMBER)
                                        .description("사용자 ID"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING)
                                        .description("문의 제목"),
                                fieldWithPath("data.question").type(JsonFieldType.STRING)
                                        .description("문의 내용")
                        )
                ));
    }

    @DisplayName("문의 상태별 질문 목록을 조회한다.")
    @Test
    void 문의_상태별_질문_목록을_조회한다() throws Exception {
        given(inquiryQuestionReadService.findAllByUserIdAndInquiryStatus(any()))
                .willReturn(List.of(
                        InquiryQuestionFindResponse.builder()
                                .inquiryQuestionId(1)
                                .title("첫 번째 문의")
                                .inquiryStatus(InquiryStatus.WAITING)
                                .build(),
                        InquiryQuestionFindResponse.builder()
                                .inquiryQuestionId(2)
                                .title("두 번째 문의")
                                .inquiryStatus(InquiryStatus.WAITING)
                                .build()
                ));

        // when // then
        mockMvc.perform(
                        get("/api/v1/inquiry/question/{inquiryStatus}", InquiryStatus.WAITING)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("inquiry-question-find",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("inquiryStatus").description("문의 상태: " + Arrays.toString(InquiryStatus.values()))
                        ),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("httpStatus").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY)
                                        .description("문의 질문 목록"),
                                fieldWithPath("data[].inquiryQuestionId").type(JsonFieldType.NUMBER)
                                        .description("문의 ID"),
                                fieldWithPath("data[].title").type(JsonFieldType.STRING)
                                        .description("문의 제목"),
                                fieldWithPath("data[].inquiryStatus").type(JsonFieldType.STRING)
                                        .description("문의 상태")
                        )
                ));
    }
}
