package docs.users;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import com.addiction.user.userCigarette.controller.UserCigaretteController;
import com.addiction.user.userCigarette.service.UserCigaretteReadService;
import com.addiction.user.userCigarette.service.UserCigaretteService;
import com.addiction.user.userCigarette.service.request.ChangeType;
import com.addiction.user.userCigarette.service.response.UserCigaretteFindResponse;
import com.addiction.user.users.controller.request.LoginRequest;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.service.request.LoginServiceRequest;
import com.addiction.user.users.service.response.LoginResponse;

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
				get("/api/v1/user/cigarette")
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
		given(userCigaretteService.changeCigaretteCount(any(ChangeType.class)))
			.willReturn(UserCigaretteFindResponse.builder()
				.count(5)
				.build()
			);

		// when // then
		mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/api/v1/user/cigarette/{changeType}", ChangeType.ADD)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("user-cigarette-change",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("changeType")
						.description("증가 (ADD), 감소(MINUS)" + Arrays.toString(ChangeType.values()))
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
					fieldWithPath("data.count").type(JsonFieldType.NUMBER)
						.description("담배 개수")
				)
			));
	}
}
