package docs.users;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import com.addiction.user.userCigarette.controller.UserCigaretteController;
import com.addiction.user.userCigarette.controller.request.UserCigaretteChangeRequest;
import com.addiction.user.userCigarette.service.UserCigaretteReadService;
import com.addiction.user.userCigarette.service.UserCigaretteService;
import com.addiction.user.userCigarette.service.request.ChangeType;
import com.addiction.user.userCigarette.service.request.UserCigaretteChangeServiceRequest;
import com.addiction.user.userCigarette.service.response.UserCigaretteFindResponse;

import docs.RestDocsSupport;

public class UserCigaretteControllerDocsTest extends RestDocsSupport {
	private final UserCigaretteService userCigaretteService = mock(UserCigaretteService.class);
	private final UserCigaretteReadService userCigaretteReadService = mock(UserCigaretteReadService.class);

	@Override
	protected Object initController() {
		return new UserCigaretteController(userCigaretteService, userCigaretteReadService);
	}

	@DisplayName("금일 핀 담배 조회 API")
	@Test
	void 금일_핀_담배_조회() throws Exception {
		// given
		given(userCigaretteReadService.findUserCigaretteCount())
			.willReturn(UserCigaretteFindResponse.builder()
				.count(5)
				.build()
			);

		// when // then
		mockMvc.perform(
				get("/api/v1/cigarette")
					.contentType(APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("user-find-cigarette",
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
					fieldWithPath("data.count").type(JsonFieldType.NUMBER)
						.description("담배 개수")
				)
			));
	}

	@DisplayName("담배 개수 수정 API")
	@Test
	void 담배_개수_수정() throws Exception {
		// given
		UserCigaretteChangeRequest request = UserCigaretteChangeRequest.builder()
			.changeType(ChangeType.ADD)
			.address("서울시 강남구 역삼동")
			.build();

		given(userCigaretteService.changeCigarette(any(UserCigaretteChangeServiceRequest.class)))
			.willReturn(1L);

		// when // then
		mockMvc.perform(
				patch("/api/v1/cigarette/change")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
					.contentType(APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("user-cigarette-change",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("changeType").type(JsonFieldType.STRING)
						.description("담배 개수 변경 타입. 가능한 값: " + Arrays.toString(ChangeType.values())),
					fieldWithPath("address").type(JsonFieldType.STRING)
						.description("흡연 장소 주소")
				),
				responseFields(
					fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
						.description("코드"),
					fieldWithPath("httpStatus").type(JsonFieldType.STRING)
						.description("상태"),
					fieldWithPath("message").type(JsonFieldType.STRING)
						.description("메세지"),
					fieldWithPath("data").type(JsonFieldType.NUMBER)
						.description("응답 데이터")
				)
			));
	}
}
