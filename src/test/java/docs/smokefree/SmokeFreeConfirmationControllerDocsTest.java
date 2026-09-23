package docs.smokefree;

import com.addiction.smokefree.controller.SmokeFreeConfirmationController;
import com.addiction.smokefree.service.SmokeFreeConfirmationService;
import com.addiction.smokefree.service.response.SmokeFreeConfirmationResponse;
import docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SmokeFreeConfirmationControllerDocsTest extends RestDocsSupport {

    private final SmokeFreeConfirmationService smokeFreeConfirmationService = mock(SmokeFreeConfirmationService.class);

    @Override
    protected Object initController() {
        return new SmokeFreeConfirmationController(smokeFreeConfirmationService);
    }

    @DisplayName("금연일 확정 API")
    @Test
    void 금연일_확정_API() throws Exception {
        given(smokeFreeConfirmationService.confirm(any()))
                .willReturn(SmokeFreeConfirmationResponse.builder().date("20260922").build());

        mockMvc.perform(post("/api/v1/smoke-free-confirmations")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"date":"20260922"}
                                """))
                .andExpect(status().isOk())
                .andDo(document("smoke-free-confirmation",
                        prettyPrint(),
                        prettyPrint(),
                        requestFields(
                                fieldWithPath("date").type(JsonFieldType.STRING).description("확정할 금연 날짜 (yyyyMMdd)")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("httpStatus").type(JsonFieldType.STRING).description("HTTP 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data.date").type(JsonFieldType.STRING).description("확정된 금연 날짜 (yyyyMMdd)")
                        )
                ));
    }
}
