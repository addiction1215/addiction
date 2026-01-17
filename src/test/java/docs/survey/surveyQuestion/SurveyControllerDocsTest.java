package docs.survey.surveyQuestion;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import com.addiction.survey.surveyAnswer.service.response.SurveyAnswerFindServiceResponse;
import com.addiction.survey.surveyQuestion.controller.SurveyQuestionController;
import com.addiction.survey.surveyQuestion.service.response.SurveyQuestionFindListServiceResponse;
import com.addiction.survey.surveyQuestion.service.response.SurveyQuestionFindServiceResponse;
import com.addiction.survey.surveyQuestion.enums.SurveyType;
import com.addiction.survey.surveyQuestion.service.SurveyQuestionReadService;

import docs.RestDocsSupport;

public class SurveyControllerDocsTest extends RestDocsSupport {

	private final SurveyQuestionReadService surveyQuestionReadService = mock(SurveyQuestionReadService.class);

	@Override
	protected Object initController() {
		return new SurveyQuestionController(surveyQuestionReadService);
	}

	@DisplayName("설문조사 문항 조회 API")
	@Test
	void 설문조사_문항_조회_API() throws Exception {
		// given
		given(surveyQuestionReadService.findAllByOrderBySortAsc())
			.willReturn(SurveyQuestionFindListServiceResponse.builder()
				.response(
					List.of(
						SurveyQuestionFindServiceResponse.builder()
							.id(1L)
							.question("현재 흡연 여부를 선택해주세요")
							.surveyType(SurveyType.CHECKBOX)
							.surveyAnswer(
								List.of(
									SurveyAnswerFindServiceResponse.builder()
										.id(1L)
										.answer("현재 흡연 중이며, 이제 금연하고 싶어요.")
										.build(),
									SurveyAnswerFindServiceResponse.builder()
										.id(1L)
										.answer("현재 금연 중이며, 계속 유지하고 싶어요.")
										.build()
								)
							)
							.build()
					)
				)
				.build()
			);

		// when // then
		mockMvc.perform(
				get("/api/v1/surveyQuestion")
					.contentType(APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("surveyQuestion-findAll",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				responseFields(
					fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
						.description("코드"),
					fieldWithPath("httpStatus").type(JsonFieldType.STRING)
						.description("상태"),
					fieldWithPath("message").type(JsonFieldType.STRING)
						.description("메세지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT)
						.description("응답 데이터"),
					fieldWithPath("data.response[]").type(JsonFieldType.ARRAY)
						.description("설문조사 문항 리스트"),
					fieldWithPath("data.response[].id").type(JsonFieldType.NUMBER)
						.description("설문조사 문항 ID"),
					fieldWithPath("data.response[].question").type(JsonFieldType.STRING)
						.description("설문조사 문항 질문"),
					fieldWithPath("data.response[].surveyType").type(JsonFieldType.STRING)
						.description("설문조사 문항 타입"),
					fieldWithPath("data.response[].surveyAnswer[]").type(JsonFieldType.ARRAY)
						.description("설문조사 문항 답변 리스트"),
					fieldWithPath("data.response[].surveyAnswer[].id").type(JsonFieldType.NUMBER)
						.description("설문조사 문항 답변 ID"),
					fieldWithPath("data.response[].surveyAnswer[].answer").type(JsonFieldType.STRING)
						.description("설문조사 문항 답변")
				)
			));
	}

}
